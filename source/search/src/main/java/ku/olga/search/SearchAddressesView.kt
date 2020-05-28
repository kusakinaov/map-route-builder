package ku.olga.search

import androidx.appcompat.widget.SearchView
import ku.olga.core_api.dto.Category
import ku.olga.core_api.dto.SearchAddress
import ku.olga.ui_core.base.BaseLocationView

interface SearchAddressesView : BaseLocationView {
    var searchView: SearchView?

    fun bindAddresses(addresses: List<SearchAddress>)
    fun showAddresses()

    fun bindCategories(categories: List<Category>)
    fun showCategories()

    fun showEmpty()
    fun showNoSearch()

    fun bindQuery(query: String?)
}