package ku.olga.search

import androidx.appcompat.widget.SearchView
import ku.olga.ui_core.base.BaseLocationView
import ku.olga.ui_core.base.BaseMapView
import org.osmdroid.views.MapView

interface SearchMapView : BaseMapView, BaseLocationView {
    var searchView: SearchView?
    var mapView: MapView?
}