package ku.olga.route_builder.presentation.search.list

import androidx.appcompat.widget.SearchView
import ku.olga.route_builder.domain.model.Category
import ku.olga.route_builder.domain.model.SearchAddress
import ku.olga.route_builder.presentation.base.BaseLocationView
import ku.olga.route_builder.presentation.base.BaseView

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