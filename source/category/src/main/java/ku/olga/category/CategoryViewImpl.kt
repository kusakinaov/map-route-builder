package ku.olga.category

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_category.view.*
import kotlinx.android.synthetic.main.fragment_category.view.mapView
import ku.olga.core_api.dto.POI
import ku.olga.core_api.dto.UserPoint
import ku.olga.core_api.mediator.EditPointMediator
import ku.olga.ui_core.utils.convertDpToPx
import ku.olga.ui_core.utils.convertSpToPx
import ku.olga.ui_core.utils.getBitmap
import ku.olga.ui_core.REQ_CODE_EDIT_POINT
import ku.olga.ui_core.REQ_CODE_LOCATION_PERMISSION
import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer
import org.osmdroid.events.DelayedMapListener
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import ku.olga.core_api.dto.BoundingBox as AppBoundingBox

class CategoryViewImpl(
        private val fragment: CategoryFragment,
        private val presenter: CategoryPresenter,
        private val editPointMediator: EditPointMediator
) : CategoryView {
    private var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>? = null

    private lateinit var markerOverlay: RadiusMarkerClusterer
    private var searchView: SearchView? = null

    init {
        fragment.view?.let {
            bottomSheetBehavior = BottomSheetBehavior.from(it.layoutContent).apply {
                addBottomSheetCallback(buildBottomSheetCallback())
                peekHeight = 0
                state = BottomSheetBehavior.STATE_HIDDEN
            }
            markerOverlay = RadiusMarkerClusterer(it.context).apply {
                setIcon(
                    getBitmap(
                        ContextCompat.getDrawable(
                            it.context,
                            R.drawable.cluster
                        )!!
                    )
                )
                textPaint.apply {
                    color = ContextCompat.getColor(it.context, R.color.map_icon_text)
                    textSize =
                        convertSpToPx(it.resources, 16f)
                }
            }
            it.mapView.apply {
                setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
                zoomController.apply {
                    setVisibility(CustomZoomButtonsController.Visibility.ALWAYS)
                    minZoomLevel = 3.0
                    maxZoomLevel = 20.0
                }
                setMultiTouchControls(true)
                addMapListener(buildMapListener())
                overlays.add(markerOverlay)
            }
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
//            queryHint = context.getString(R.string.hint_search_address)
            setOnQueryTextListener(buildOnQueryTextListener())
        }
    }

    private fun buildOnQueryTextListener() = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?) = true

        override fun onQueryTextChange(newText: String?): Boolean {
            presenter.onQueryChanged(newText)
//            addressesAdapter.setQuery(newText)
            return true
        }
    }

    private fun buildBottomSheetCallback() = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
        }

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            when (newState) {
                BottomSheetBehavior.STATE_EXPANDED -> fragment.view?.buttonAdd?.visibility =
                        View.VISIBLE
                BottomSheetBehavior.STATE_HIDDEN,
                BottomSheetBehavior.STATE_COLLAPSED -> {
                    fragment.view?.buttonAdd?.apply {
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
            presenter.onBoundingBoxChanged(it.latitude, it.longitude,
                    mapView.boundingBox.toAppBoundingBox(), mapView.zoomLevelDouble)
        }
    }

    override fun onAttach() {
        presenter.attachView(this)
    }

    override fun onResume() {
        fragment.view?.mapView?.onResume()
    }

    override fun onPause() {
        fragment.view?.mapView?.onPause()
    }

    override fun setPOIs(pois: List<POI>) {
        markerOverlay.items.clear()
        fragment.context?.let {
            val poiIcon = ContextCompat.getDrawable(it, R.drawable.ic_place)
            for (poi in pois) {
                markerOverlay.add(buildMarker(poi, poiIcon))
            }
        }
        markerOverlay.invalidate()
    }

    private fun buildMarker(poi: POI, poiIcon: Drawable?): Marker =
            Marker(fragment.view?.mapView).apply {
                title = poi.title
                snippet = poi.description
                position = GeoPoint(poi.latitude, poi.longitude)
                icon = poiIcon
                setOnMarkerClickListener { _, _ -> showPOIDetails(poi) }
            }

    private fun showPOIDetails(poi: POI): Boolean {
        fragment.view?.apply {
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
            peekHeight = fragment.view?.layoutContent?.measuredHeight ?: 0
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

    override fun moveTo(latitude: Double, longitude: Double, zoomLevel: Double, animate: Boolean) {
        fragment.view?.mapView?.controller?.apply {
            animateTo(
                    GeoPoint(latitude, longitude),
                    zoomLevel, if (animate) DEFAULT_MOVE_SPEED else NONE_MOVE_SPEED
            )
        }
    }

    override fun moveTo(pois: List<POI>, animate: Boolean) {
        val boundingBox = buildBoundingBox(pois)
        fragment.view?.mapView?.apply {
            post {
                zoomToBoundingBox(boundingBox, animate, convertDpToPx(
                    resources,
                    BORDER_SIZE
                ).toInt())
            }
        }
    }

    override fun openEditPOI(userPoint: UserPoint) {
        editPointMediator.editPoint(fragment, REQ_CODE_EDIT_POINT, userPoint)
    }

    override fun bindQuery(query: String?) {
        searchView?.setQuery(query, false)
    }

    override fun hasLocationPermission(): Boolean {
        fragment.context?.let {
            return ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        }
        return false
    }

    override fun requestLocationPermission() {
        fragment.requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQ_CODE_LOCATION_PERMISSION
        )
    }

    private fun buildBoundingBox(pois: List<POI>) =
            BoundingBox.fromGeoPointsSafe(pois.map { GeoPoint(it.latitude, it.longitude) })

    override fun onDetach() {
        presenter.detachView()
    }

    override fun showDefaultError() {
        fragment.context?.let { fragment.showSnackbar(it.getString(R.string.error_search_pois)) }
    }

    private fun BoundingBox.toAppBoundingBox() =
            AppBoundingBox(latNorth, lonEast, latSouth, lonWest)

    companion object {
        private const val DELAY_LOAD_POI = 1000L
        private const val NONE_MOVE_SPEED = 0L
        private const val DEFAULT_MOVE_SPEED = 500L
        private const val BORDER_SIZE = 40f
    }
}