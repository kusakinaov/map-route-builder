package ku.olga.route_builder.presentation.search.list

import ku.olga.route_builder.domain.model.SearchAddress
import ku.olga.route_builder.presentation.base.BaseView

interface SearchAddressesView : BaseView {
    fun showEmpty()
    fun hasLocationPermission(): Boolean
    fun requestLocationPermission()
    fun bindQuery(query: String?)
    fun showAddresses(addresses: List<SearchAddress>)
}