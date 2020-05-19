package ku.olga.route_builder.presentation.search.category

import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.CustomZoomButtonsController
import kotlinx.android.synthetic.main.fragment_category.view.*

class CategoryViewImpl(private val fragment: CategoryFragment,
    private val presenter: CategoryPresenter) : CategoryView {
    private var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>? = null

    init {
        fragment.view?.let {
            bottomSheetBehavior = BottomSheetBehavior.from(it.layoutContent).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }
            it.mapView.apply {
                setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
                zoomController.setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT)
                setMultiTouchControls(true)
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

    override fun onDetach() {
        presenter.detachView()
    }
}