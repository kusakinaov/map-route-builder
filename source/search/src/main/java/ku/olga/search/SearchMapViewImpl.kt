package ku.olga.search

import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import ku.olga.ui_core.REQ_CODE_LOCATION_PERMISSION
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

class SearchMapViewImpl(
    private val fragment: Fragment,
    private val presenter: SearchMapPresenter
) : SearchMapView {
    override var searchView: SearchView? = null
    override var mapView: MapView? = null

    override fun hasLocationPermission(): Boolean = ku.olga.ui_core.utils.hasLocationPermission(fragment.context)

    override fun requestLocationPermission() {
        ku.olga.ui_core.utils.requestLocationPermission(fragment, REQ_CODE_LOCATION_PERMISSION)
    }

    override fun onResume() {
        mapView?.onResume()

    }

    override fun onPause() {
        mapView?.onPause()
    }

    override fun moveTo(latitude: Double, longitude: Double, zoomLevel: Double, animated: Boolean) {
        mapView?.let {
            ku.olga.ui_core.view.moveTo(it, latitude, longitude, zoomLevel, animated)
        }
    }

    override fun moveTo(geoPoints: List<GeoPoint>, animated: Boolean) {
        mapView?.let {
            ku.olga.ui_core.view.moveTo(it, geoPoints, animated)
        }
    }

    override fun onAttach() {
        presenter.attachView(this)
    }

    override fun onDetach() {
        presenter.detachView()
    }
}