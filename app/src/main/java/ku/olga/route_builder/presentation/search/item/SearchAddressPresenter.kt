package ku.olga.route_builder.presentation.search.item

import kotlinx.coroutines.*
import ku.olga.route_builder.domain.model.SearchAddress
import ku.olga.route_builder.domain.services.PointsService
import ku.olga.route_builder.presentation.base.BasePresenter
import java.io.IOException

class SearchAddressPresenter(private val pointsService: PointsService) :
    BasePresenter<SearchAddressView>() {
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
        searchAddress?.let {
            view?.editPoint(it.postalAddress, it.lat, it.lon)
        }
    }

    fun onCoordinatesChanged(latitude: Double?, longitude: Double?) {
        trySearchAddress(latitude, longitude)
    }

    private fun searchAddress(latitude: Double, longitude: Double) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val addresses = pointsService.searchAddress(latitude, longitude)
                withContext(Dispatchers.Main) {
                    if (addresses.isNotEmpty()) {
                        searchAddress = addresses[0]
                    }
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) { view?.showDefaultError() }
            }
        }


    private fun trySearchAddress(latitude: Double?, longitude: Double?) {
        job?.apply { if (isActive) cancel() }
        if (latitude != null && longitude != null) {
            job = searchAddress(latitude, longitude)
        }
    }

    fun onClickRetry(latitude: Double?, longitude: Double?) {
        trySearchAddress(latitude, longitude)
    }
}