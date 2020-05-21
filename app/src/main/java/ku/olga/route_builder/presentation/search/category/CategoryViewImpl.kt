package ku.olga.route_builder.presentation.search.category

import android.graphics.drawable.Drawable
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_category.view.*
import ku.olga.route_builder.R
import ku.olga.route_builder.domain.model.Coordinates
import ku.olga.route_builder.domain.model.POI
import ku.olga.route_builder.presentation.convertSpToPx
import ku.olga.route_builder.presentation.getBitmap
import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer
import org.osmdroid.events.DelayedMapListener
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.Marker
import ku.olga.route_builder.domain.model.BoundingBox as AppBoundingBox

class CategoryViewImpl(private val fragment: CategoryFragment,
                       private val presenter: CategoryPresenter) : CategoryView {
    private var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>? = null

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
                zoomController.setVisibility(CustomZoomButtonsController.Visibility.ALWAYS)
                setMultiTouchControls(true)
                addMapListener(buildMapListener())
                overlays.add(markerOverlay)
            }
        }
    }

    private fun buildBottomSheetCallback() = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
        }

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            when (newState) {
                BottomSheetBehavior.STATE_EXPANDED -> fragment.view?.buttonAdd?.visibility = View.VISIBLE
                BottomSheetBehavior.STATE_HIDDEN,
                BottomSheetBehavior.STATE_COLLAPSED -> fragment.view?.buttonAdd?.visibility = View.GONE
            }
        }
    }

    private fun buildMapListener() = DelayedMapListener(object : MapListener {
        override fun onScroll(event: ScrollEvent?): Boolean {
            event?.source?.let {
                presenter.onBoundingBoxChanged(it.mapCenter.latitude, it.mapCenter.longitude,
                        it.boundingBox.toAppBoundingBox())
            }
            return true
        }

        override fun onZoom(event: ZoomEvent?): Boolean {
            event?.source?.let {
                presenter.onBoundingBoxChanged(it.mapCenter.latitude, it.mapCenter.longitude,
                        it.boundingBox.toAppBoundingBox())
            }
            return true
        }
    }, DELAY_LOAD_POI)

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

    override fun moveTo(latitude: Double, longitude: Double, animate: Boolean) {
        fragment.view?.mapView?.controller?.apply {
            animateTo(GeoPoint(latitude, longitude),
                    DEFAULT_ZOOM_LEVEL, if (animate) DEFAULT_MOVE_SPEED else NONE_MOVE_SPEED)
        }
    }

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
        private const val DEFAULT_ZOOM_LEVEL = 15.0
    }
}