package ku.olga.route_builder.presentation.search.item

import kotlinx.coroutines.*
import ku.olga.route_builder.domain.model.SearchAddress
import ku.olga.route_builder.domain.repository.PointsRepository
import ku.olga.route_builder.presentation.base.BasePresenter

class SearchAddressPresenter(val pointsRepository: PointsRepository) : BasePresenter<SearchAddressView>() {
    var searchAddress: SearchAddress? = null
        set(value) {
            field = value
            bindSearchAddress()
        }
    var job: Job? = null

    override fun attachView(view: SearchAddressView) {
        super.attachView(view)
        bindSearchAddress()
    }

    override fun detachView() {
        job?.cancel()
        super.detachView()
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

    fun onCoordinatesChanged(latitude: Double, longitude: Double) {
        job?.apply { if (isActive) cancel() }
        job = CoroutineScope(Dispatchers.IO).launch {
            val addresses = pointsRepository.searchAddress(latitude, longitude)
            withContext(Dispatchers.Main) {
                if (addresses.isNotEmpty()) {
                    searchAddress = addresses[0]
                }
            }
        }
    }
}