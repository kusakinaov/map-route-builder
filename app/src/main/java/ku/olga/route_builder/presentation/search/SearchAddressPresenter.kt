package ku.olga.route_builder.presentation.search

import ku.olga.route_builder.domain.model.SearchAddress
import ku.olga.route_builder.presentation.base.BasePresenter

class SearchAddressPresenter : BasePresenter<SearchAddressView>() {
    var searchAddress: SearchAddress? = null
        set(value) {
            field = value
            bindSearchAddress()
        }

    override fun attachView(view: SearchAddressView) {
        super.attachView(view)
        bindSearchAddress()
    }

    fun bindSearchAddress() {
        searchAddress?.let {
            view?.apply {
                bindLatLng(it.lat, it.lon)
                bindAddress(it.postalAddress)
            }
        }
    }

    fun onClickAdd() {

    }
}