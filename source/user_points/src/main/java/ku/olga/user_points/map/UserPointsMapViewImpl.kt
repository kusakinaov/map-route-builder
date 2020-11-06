package ku.olga.user_points.map

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_user_points_map.view.*
import ku.olga.core_api.dto.Coordinates
import ku.olga.core_api.dto.UserPoint
import ku.olga.ui_core.view.buildDirectionsPolyline
import ku.olga.ui_core.view.buildRadiusMarkerClusterer
import ku.olga.ui_core.view.initMapView
import ku.olga.user_points.R
import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

abstract class UserPointsMapViewImpl(
    private val view: View,
    private val presenter: UserPointsMapPresenter,
    private val bottomSheetCallback: UserPointsMapFragment.BottomSheetCallback?
) : UserPointsMapView {
    private var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>? = null
    private var markerOverlay: RadiusMarkerClusterer
    private var directionsPolyline: Polyline

    init {
        view.let {
            bottomSheetBehavior = BottomSheetBehavior.from(it.layoutContent).apply {
                addBottomSheetCallback(buildBottomSheetCallback())
                state = BottomSheetBehavior.STATE_HIDDEN
            }
            markerOverlay = buildRadiusMarkerClusterer(
                it.context,
                ContextCompat.getDrawable(it.context, R.drawable.cluster)!!,
                ContextCompat.getColor(it.context, R.color.map_icon_text)
            )
            directionsPolyline = buildDirectionsPolyline(
                it.context,
                ContextCompat.getColor(it.context, R.color.map_route)
            )

            initMapView(
                it.mapView,
                buildMapListener(),
                arrayListOf(markerOverlay, directionsPolyline)
            )

            it.buttonEdit.setOnClickListener {
                if (it.tag is UserPoint) {
                    presenter.onClickEditUserPoint(it.tag as UserPoint)
                }
            }
        }
    }

    private fun buildMapListener() = object : MapListener {
        override fun onScroll(event: ScrollEvent?): Boolean {
            event?.source?.let { onMapChanged(it) }
            return true
        }

        override fun onZoom(event: ZoomEvent?): Boolean {
            event?.source?.let { onMapChanged(it) }
            return true
        }
    }

    private fun onMapChanged(mapView: MapView) {
        mapView.mapCenter.let {
            presenter.onCenterChanged(it.latitude, it.longitude, mapView.zoomLevelDouble)
        }
    }

    override fun onAttach() {
        presenter.attachView(this)
    }

    private fun buildBottomSheetCallback() = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
        }

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            when (newState) {
                BottomSheetBehavior.STATE_EXPANDED -> {
                    view.buttonEdit?.visibility = View.VISIBLE
                }
                BottomSheetBehavior.STATE_HIDDEN,
                BottomSheetBehavior.STATE_COLLAPSED -> {
                    view.buttonEdit?.visibility = View.GONE
                    bottomSheetCallback?.onHide()
                }
            }
        }
    }

    override fun onResume() {
        view.mapView?.onResume()
    }

    override fun onPause() {
        view.mapView?.onPause()
    }

    override fun hideUserPoint(): Boolean {
        val expanded = bottomSheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED
        if (expanded) {
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
            view.buttonEdit?.visibility = View.GONE
        }
        return expanded
    }

    override fun showUserPoint(userPoint: UserPoint) {
        view.apply {
            textViewTitle.text = userPoint.title
            textViewDescription.apply {
                text = userPoint.description
                visibility = if (userPoint.description.isNullOrEmpty()) View.GONE else View.VISIBLE
            }
            textViewAddress.text = userPoint.postalAddress
            buttonEdit.apply {
                tag = userPoint
                visibility = View.VISIBLE
            }
        }
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetCallback?.onShown()
    }

    override fun showDirectionsError() {
        showError(view.resources.getString(R.string.error_build_directions))
    }

    override fun showDirections(coordinates: List<Coordinates>) {
        directionsPolyline.setPoints(coordinates.map { GeoPoint(it.latitude, it.longitude) })
        view.mapView?.invalidate()
    }

    override fun onDetach() {
        presenter.detachView()
    }

    override fun setUserPoints(userPoints: List<UserPoint>) {
        markerOverlay.items.clear()
        view.context?.let {
            val poiIcon = ContextCompat.getDrawable(it, R.drawable.ic_place)
            for (poi in userPoints) {
                markerOverlay.add(buildMarker(poi, poiIcon))
            }
        }
        markerOverlay.invalidate()
    }

    override fun moveTo(latitude: Double, longitude: Double, zoomLevel: Double, animated: Boolean) {
        view.mapView?.let {
            ku.olga.ui_core.view.moveTo(it, latitude, longitude, zoomLevel, animated)
        }
    }

    override fun moveTo(geoPoints: List<GeoPoint>, animated: Boolean) {
        view.mapView?.let { ku.olga.ui_core.view.moveTo(it, geoPoints, animated) }
    }

    private fun buildMarker(point: UserPoint, poiIcon: Drawable?): Marker =
        Marker(view.mapView).apply {
            title = point.title
            snippet = point.description
            position = GeoPoint(point.lat, point.lon)
            icon = poiIcon
            setOnMarkerClickListener { _, _ -> presenter.onClickMarker(point) }
        }

    override fun showError(error: CharSequence) {
        Snackbar.make(view, error, Snackbar.LENGTH_SHORT).show()
    }
}
