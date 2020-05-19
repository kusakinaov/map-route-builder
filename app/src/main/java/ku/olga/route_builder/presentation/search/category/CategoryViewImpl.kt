package ku.olga.route_builder.presentation.search.category

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_category.view.*
import ku.olga.route_builder.R
import ku.olga.route_builder.domain.model.POI
import org.osmdroid.events.DelayedMapListener
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.FolderOverlay
import org.osmdroid.views.overlay.Marker
import ku.olga.route_builder.domain.model.BoundingBox as AppBoundingBox

class CategoryViewImpl(private val fragment: CategoryFragment,
    private val presenter: CategoryPresenter) : CategoryView {
    private var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>? = null
    private val folderOverlay = FolderOverlay()

    init {
        fragment.view?.let {
            bottomSheetBehavior = BottomSheetBehavior.from(it.layoutContent).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }
            it.mapView.apply {
                setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
                zoomController.setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT)
                setMultiTouchControls(true)
                addMapListener(DelayedMapListener(object : MapListener {
                    override fun onScroll(event: ScrollEvent?): Boolean {
                        presenter.onBoundingBoxChanged(boundingBox.toAppBoundingBox())
                        return true
                    }

                    override fun onZoom(event: ZoomEvent?): Boolean {
                        presenter.onBoundingBoxChanged(boundingBox.toAppBoundingBox())
                        return true
                    }
                }, DELAY_LOAD_POI))
                overlays.add(folderOverlay)
            }
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

    override fun showPOIs(pois: List<POI>) {
        folderOverlay.items.clear()
        fragment.context?.let {
            val poiIcon = ContextCompat.getDrawable(it, R.drawable.ic_place)
            for (poi in pois) {
                folderOverlay.add(Marker(fragment.view?.mapView).apply {
                    title = poi.title
                    snippet = poi.description
                    position = GeoPoint(poi.latitude, poi.longitude)
                    icon = poiIcon
                    setOnMarkerClickListener { marker, mapView -> true }
                })
            }
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
        private const val DELAY_LOAD_POI = 500L
    }
}