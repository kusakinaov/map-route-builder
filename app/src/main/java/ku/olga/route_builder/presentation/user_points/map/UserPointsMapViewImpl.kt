package ku.olga.route_builder.presentation.user_points.map

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_user_points_map.*
import kotlinx.android.synthetic.main.fragment_user_points_map.view.*
import ku.olga.route_builder.R
import ku.olga.route_builder.REQ_CODE_EDIT_POINT
import ku.olga.route_builder.domain.model.UserPoint
import ku.olga.route_builder.presentation.base.BaseFragment
import ku.olga.route_builder.presentation.convertDpToPx
import ku.olga.route_builder.presentation.convertSpToPx
import ku.olga.route_builder.presentation.getBitmap
import ku.olga.route_builder.presentation.point.EditPointFragment
import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class UserPointsMapViewImpl(
        private val fragment: Fragment,
        private val presenter: UserPointsMapPresenter
) : UserPointsMapView {
    private var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>? = null
    private lateinit var markerOverlay: RadiusMarkerClusterer

    init {
        fragment.view?.let {
            bottomSheetBehavior = BottomSheetBehavior.from(it.layoutContent).apply {
                addBottomSheetCallback(buildBottomSheetCallback())
                state = BottomSheetBehavior.STATE_HIDDEN
            }
            markerOverlay = RadiusMarkerClusterer(it.context).apply {
                setIcon(getBitmap(ContextCompat.getDrawable(it.context, R.drawable.cluster)!!))
                textPaint.apply {
                    color = ContextCompat.getColor(it.context, R.color.map_icon_text)
                    textSize = convertSpToPx(it.resources, 16f)
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
                    fragment.buttonEdit?.visibility = View.VISIBLE
                }
                BottomSheetBehavior.STATE_HIDDEN,
                BottomSheetBehavior.STATE_COLLAPSED -> {
                    fragment.buttonEdit?.visibility = View.GONE
                    val parent = fragment.parentFragment
                    if (parent is UserPointsMapFragment.BottomSheetCallback) {
                        parent.onHide()
                    }
                }
            }
        }
    }

    override fun onResume() {
        fragment.mapView?.onResume()
    }

    override fun onPause() {
        fragment.mapView?.onPause()
    }

    override fun hideUserPoint(): Boolean {
        val expanded = bottomSheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED
        if (expanded) {
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
            fragment.buttonEdit?.visibility = View.GONE
        }
        return expanded
    }

    override fun editUserPoint(userPoint: UserPoint) {
        val parent = fragment.parentFragment
        if (parent is BaseFragment) {
            parent.replaceFragment(
                    EditPointFragment
                            .newInstance(parent, REQ_CODE_EDIT_POINT, userPoint), true
            )
        }
    }

    override fun showUserPoint(userPoint: UserPoint) {
        fragment.view?.apply {
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
        val parent = fragment.parentFragment
        if (parent is UserPointsMapFragment.BottomSheetCallback) {
            parent.onShown()
        }
    }

    override fun onDetach() {
        presenter.detachView()
    }

    override fun setUserPoints(userPoints: List<UserPoint>) {
        markerOverlay.items.clear()
        fragment.context?.let {
            val poiIcon = ContextCompat.getDrawable(it, R.drawable.ic_place)
            for (poi in userPoints) {
                markerOverlay.add(buildMarker(poi, poiIcon))
            }
        }
        markerOverlay.invalidate()
    }

    override fun moveTo(latitude: Double, longitude: Double, zoomLevel: Double, animated: Boolean) {
        fragment.mapView?.controller?.animateTo(
                GeoPoint(latitude, longitude),
                zoomLevel, if (animated) DEFAULT_MOVE_SPEED else NONE_MOVE_SPEED
        )
    }

    private fun buildMarker(point: UserPoint, poiIcon: Drawable?): Marker =
            Marker(fragment.view?.mapView).apply {
                title = point.title
                snippet = point.description
                position = GeoPoint(point.lat, point.lon)
                icon = poiIcon
                setOnMarkerClickListener { _, _ -> presenter.onClickMarker(point) }
            }

    override fun moveTo(userPoints: List<UserPoint>, animated: Boolean) {
        val boundingBox = buildBoundingBox(userPoints)
        fragment.mapView?.apply {
            post {
                zoomToBoundingBox(boundingBox, animated, convertDpToPx(resources, BORDER_SIZE).toInt())
            }
        }
    }

    private fun buildBoundingBox(userPoints: List<UserPoint>) =
            BoundingBox.fromGeoPointsSafe(userPoints.map { GeoPoint(it.lat, it.lon) })

    companion object {
        private const val NONE_MOVE_SPEED = 0L
        private const val DEFAULT_MOVE_SPEED = 500L
        private const val BORDER_SIZE = 40f
    }
}
