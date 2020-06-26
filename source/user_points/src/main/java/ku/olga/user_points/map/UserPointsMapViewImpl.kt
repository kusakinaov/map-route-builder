package ku.olga.user_points.map

import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_user_points_map.*
import kotlinx.android.synthetic.main.fragment_user_points_map.view.*
import ku.olga.core_api.dto.Coordinates
import ku.olga.ui_core.REQ_CODE_EDIT_POINT
import ku.olga.core_api.dto.UserPoint
import ku.olga.core_api.mediator.EditPointMediator
import ku.olga.ui_core.base.BaseFragment
import ku.olga.ui_core.utils.convertDpToPx
import ku.olga.ui_core.utils.convertSpToPx
import ku.olga.ui_core.utils.getBitmap
import ku.olga.user_points.R
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
import org.osmdroid.views.overlay.Polyline

class UserPointsMapViewImpl(
    private val fragment: UserPointsMapFragment,
    private val presenter: UserPointsMapPresenter,
    private val editPointMediator: EditPointMediator
) : UserPointsMapView {
    private var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>? = null
    private lateinit var markerOverlay: RadiusMarkerClusterer
    private lateinit var directionsPolyline: Polyline

    init {
        fragment.view?.let {
            bottomSheetBehavior = BottomSheetBehavior.from(it.layoutContent).apply {
                addBottomSheetCallback(buildBottomSheetCallback())
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

                directionsPolyline = Polyline().apply {
                    outlinePaint.apply {
                        isAntiAlias = true
                        color = ContextCompat.getColor(context, R.color.map_route)
                        strokeWidth = convertDpToPx(resources, 4f)
                        style = Paint.Style.STROKE
                    }
                    isGeodesic = true
                }
                overlays.add(directionsPolyline)
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
            editPointMediator.editPoint(
                parent,
                REQ_CODE_EDIT_POINT, userPoint
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

    override fun showDirectionsError() {
        showError(fragment.getString(R.string.error_build_directions))
    }

    override fun showDirections(coordinates: List<Coordinates>) {
        directionsPolyline.setPoints(coordinates.map { GeoPoint(it.latitude, it.longitude) })
        fragment.mapView?.invalidate()
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
                zoomToBoundingBox(
                    boundingBox, animated, convertDpToPx(
                        resources,
                        BORDER_SIZE
                    ).toInt()
                )
            }
        }
    }

    private fun buildBoundingBox(userPoints: List<UserPoint>) =
        BoundingBox.fromGeoPointsSafe(userPoints.map { GeoPoint(it.lat, it.lon) })

    override fun showError(error: CharSequence) {
        fragment.showSnackbar(error)
    }

    companion object {
        private const val NONE_MOVE_SPEED = 0L
        private const val DEFAULT_MOVE_SPEED = 500L
        private const val BORDER_SIZE = 40f
    }
}
