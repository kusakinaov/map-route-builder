package ku.olga.search

import androidx.appcompat.widget.SearchView
import ku.olga.core_api.dto.*
import ku.olga.ui_core.base.BaseLocationView
import ku.olga.ui_core.base.BaseMapView
import ku.olga.ui_core.utils.OnKeyboardVisibilityListener

interface SearchMapView : BaseMapView, BaseLocationView, OnKeyboardVisibilityListener {
    var searchView: SearchView?

    fun bindQuery(query: String?)
    fun bindBoundingBox(boundingBox: BoundingBox)

    fun bindCategories(categories: List<Category>)
    fun bindAddresses(addresses: List<SearchAddress>)
    fun bindPOIs(pois: List<POI>)

    fun hideAll()

    fun showPOIs(category: Category?)
    fun showAddresses()
    fun showCategories()
    fun bindClearButton(visible: Boolean)

    fun closeBottomSheet(): Boolean

    fun showEditDialog(userPoint: UserPoint)
}