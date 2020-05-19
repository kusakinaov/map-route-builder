package ku.olga.route_builder.presentation.search.list

import androidx.appcompat.widget.SearchView
import ku.olga.route_builder.domain.model.SearchAddress
import ku.olga.route_builder.presentation.base.BaseView

interface SearchAddressesView : BaseView {
    var searchView: SearchView?

    fun bindAddresses(addresses: List<SearchAddress>)
    fun showEmpty()
    fun showAddresses()
    fun showNoSearch()

    fun hasLocationPermission(): Boolean
    fun requestLocationPermission()
    fun bindQuery(query: String?)
}