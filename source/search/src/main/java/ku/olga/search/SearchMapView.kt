package ku.olga.search

import androidx.appcompat.widget.SearchView
import ku.olga.core_api.dto.BoundingBox
import ku.olga.core_api.dto.Category
import ku.olga.core_api.dto.POI
import ku.olga.core_api.dto.SearchAddress
import ku.olga.ui_core.base.BaseLocationView
import ku.olga.ui_core.base.BaseMapView
import org.osmdroid.views.MapView

interface SearchMapView : BaseMapView, BaseLocationView {
    var searchView: SearchView?
    var mapView: MapView?

    fun bindQuery(query: String?)
    fun bindCategory(category: Category?)
    fun bindBoundingBox(boundingBox: BoundingBox)

    fun showCategories(categories: List<Category>)
    fun showAddresses(addresses: List<SearchAddress>)
    fun showPOIs(pois: List<POI>)
    fun hideAll()
}