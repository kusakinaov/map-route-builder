package ku.olga.search

import androidx.appcompat.widget.SearchView
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

class SearchMapViewImpl : SearchMapView {
    override var searchView: SearchView? = null
    override var mapView: MapView? = null

    override fun onResume() {
        TODO("Not yet implemented")
    }

    override fun onPause() {
        TODO("Not yet implemented")
    }

    override fun moveTo(latitude: Double, longitude: Double, zoomLevel: Double, animated: Boolean) {
        TODO("Not yet implemented")
    }

    override fun moveTo(geoPoint: List<GeoPoint>, animated: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onAttach() {
        TODO("Not yet implemented")
    }

    override fun onDetach() {
        TODO("Not yet implemented")
    }
}