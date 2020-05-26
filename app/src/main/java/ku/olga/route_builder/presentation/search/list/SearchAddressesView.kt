package ku.olga.route_builder.presentation.search.list

import androidx.appcompat.widget.SearchView
import ku.olga.core_api.dto.Category
import ku.olga.core_api.dto.SearchAddress
import ku.olga.route_builder.presentation.base.BaseLocationView

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