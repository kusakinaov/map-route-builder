package ku.olga.category

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_category.view.*
import kotlinx.android.synthetic.main.fragment_category.view.mapView
import ku.olga.core_api.dto.POI
import ku.olga.ui_core.view.buildRadiusMarkerClusterer
import ku.olga.ui_core.view.initMapView
import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer
import org.osmdroid.events.DelayedMapListener
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import ku.olga.core_api.dto.BoundingBox as AppBoundingBox

abstract class CategoryViewImpl(
    private val view: View,
    private val presenter: CategoryPresenter
) : CategoryView {
    private var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>? = null

    private var markerOverlay: RadiusMarkerClusterer
    private var searchView: SearchView? = null

    init {
        view.let {
            bottomSheetBehavior = BottomSheetBehavior.from(it.layoutContent).apply {
                addBottomSheetCallback(buildBottomSheetCallback())
                peekHeight = 0
                state = BottomSheetBehavior.STATE_HIDDEN
            }

            markerOverlay = buildRadiusMarkerClusterer(
                it.context,
                ContextCompat.getDrawable(it.context, R.drawable.cluster)!!,
                ContextCompat.getColor(it.context, R.color.map_icon_text)
            )
            initMapView(it.mapView, buildMapListener(), listOf(markerOverlay))

            it.buttonAdd.setOnClickListener {
                it.tag.let {
                    if (it is POI) presenter.onClickAddPOI(it)
                }
            }
        }
    }

    override fun setSearchView(searchView: SearchView?) {
        this.searchView = searchView
        presenter.bindQuery()
        searchView?.apply {
            setOnQueryTextListener(buildOnQueryTextListener())
        }
    }

    private fun buildOnQueryTextListener() = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?) = true

        override fun onQueryTextChange(newText: String?): Boolean {
            presenter.onQueryChanged(newText)
            return true
        }
    }

    private fun buildBottomSheetCallback() = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
        }

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            when (newState) {
                BottomSheetBehavior.STATE_EXPANDED -> view.buttonAdd?.visibility =
                    View.VISIBLE
                BottomSheetBehavior.STATE_HIDDEN,
                BottomSheetBehavior.STATE_COLLAPSED -> {
                    view.buttonAdd?.apply {
                        visibility = View.GONE
                        tag = null
                    }
                }
            }
        }
    }

    private fun buildMapListener() = DelayedMapListener(object : MapListener {
        override fun onScroll(event: ScrollEvent?): Boolean {
            event?.source?.let { onMapChanged(it) }
            return true
        }

        override fun onZoom(event: ZoomEvent?): Boolean {
            event?.source?.let { onMapChanged(it) }
            return true
        }
    }, DELAY_LOAD_POI)

    private fun onMapChanged(mapView: MapView) {
        mapView.mapCenter.let {
            presenter.onBoundingBoxChanged(
                it.latitude, it.longitude,
                mapView.boundingBox.toAppBoundingBox(), mapView.zoomLevelDouble
            )
        }
    }

    override fun onAttach() {
        presenter.attachView(this)
    }

    override fun onResume() {
        view.mapView?.onResume()
    }

    override fun onPause() {
        view.mapView?.onPause()
    }

    override fun setPOIs(pois: List<POI>) {
        markerOverlay.items.clear()
        view.context?.let {
            val poiIcon = ContextCompat.getDrawable(it, R.drawable.ic_place)
            for (poi in pois) {
                markerOverlay.add(buildMarker(poi, poiIcon))
            }
        }
        markerOverlay.invalidate()
    }

    private fun buildMarker(poi: POI, poiIcon: Drawable?): Marker =
        Marker(view.mapView).apply {
            title = poi.title
            snippet = poi.description
            position = GeoPoint(poi.latitude, poi.longitude)
            icon = poiIcon
            setOnMarkerClickListener { _, _ -> showPOIDetails(poi) }
        }

    private fun showPOIDetails(poi: POI): Boolean {
        view.apply {
            mapView.controller.animateTo(GeoPoint(poi.latitude, poi.longitude))
            textViewTitle.apply {
                text = poi.title
                visibility = if (text.isNullOrEmpty()) View.GONE else View.VISIBLE
            }
            textViewDescription.apply {
                text = poi.description
                visibility = if (text.isNullOrEmpty()) View.GONE else View.VISIBLE
            }
            buttonAdd.tag = poi
        }
        bottomSheetBehavior?.apply {
            peekHeight = view.layoutContent?.measuredHeight ?: 0
            state = BottomSheetBehavior.STATE_EXPANDED
        }
        return true
    }

    override fun hidePOIDetails(): Boolean {
        if (bottomSheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
            return true
        }
        return false
    }

    override fun moveTo(latitude: Double, longitude: Double, zoomLevel: Double, animated: Boolean) {
        view.mapView?.let {
            ku.olga.ui_core.view.moveTo(
                it,
                latitude,
                longitude,
                zoomLevel,
                animated
            )
        }
    }

    override fun moveTo(geoPoints: List<GeoPoint>, animated: Boolean) {
        view.mapView?.let { ku.olga.ui_core.view.moveTo(it, geoPoints, animated) }
    }

    override fun bindQuery(query: String?) {
        searchView?.setQuery(query, false)
    }

    override fun hasLocationPermission(): Boolean {
        view.context?.let {
            return ContextCompat.checkSelfPermission(
                it,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        }
        return false
    }

    override fun onDetach() {
        presenter.detachView()
    }

    override fun showDefaultError() {
        Snackbar.make(
            view,
            view.resources.getString(R.string.error_search_pois),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun BoundingBox.toAppBoundingBox() =
        AppBoundingBox(latNorth, lonEast, latSouth, lonWest)

    companion object {
        private const val DELAY_LOAD_POI = 1000L
    }
}