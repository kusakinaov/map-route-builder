package ku.olga.category

import androidx.appcompat.widget.SearchView
import ku.olga.core_api.dto.POI
import ku.olga.core_api.dto.UserPoint
import ku.olga.ui_core.base.BaseLocationView
import ku.olga.ui_core.base.BaseMapView

interface CategoryView : BaseMapView, BaseLocationView {
    fun setPOIs(pois: List<POI>)
    fun hidePOIDetails(): Boolean
    fun openEditPOI(userPoint: UserPoint)
    fun bindQuery(query: String?)
    fun setSearchView(searchView: SearchView?)
}