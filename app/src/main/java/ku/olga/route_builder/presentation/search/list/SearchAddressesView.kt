package ku.olga.route_builder.presentation.search.list

import ku.olga.route_builder.domain.model.SearchAddress
import ku.olga.route_builder.presentation.base.BaseView

interface SearchAddressesView : BaseView {
    fun bindAddresses(addresses: List<SearchAddress>)
    fun showEmpty()
    fun showAddresses()
    fun showNoSearch()

    fun hasLocationPermission(): Boolean
    fun requestLocationPermission()
    fun bindQuery(query: String?)
}