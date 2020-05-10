package ku.olga.route_builder.presentation.search.list

import android.location.Location
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.*
import ku.olga.route_builder.domain.model.SearchAddress
import ku.olga.route_builder.domain.repository.AddressRepository
import ku.olga.route_builder.presentation.base.BasePresenter
import java.io.IOException
import javax.inject.Inject

class SearchAddressesPresenter @Inject constructor(private val addressRepository: AddressRepository) :
    BasePresenter<SearchAddressesView>() {
    var locationClient: FusedLocationProviderClient? = null

    private var query: String? = null
    private var location: Location? = null
    private var requestingLocationUpdates: Boolean = false
    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return

            for (l in locationResult.locations) {
                location = l
            }
        }
    }
    private var job: Job? = null
    private val addresses = mutableListOf<SearchAddress>()

    override fun attachView(view: SearchAddressesView) {
        super.attachView(view)
        view.apply {
            if (!hasLocationPermission()) {
                requestLocationPermission()
            } else {
                startLocationUpdates()
            }
        }
        bindQuery()
        bindAddresses()
    }

    override fun detachView() {
        stopLocationUpdates()
        super.detachView()
    }

    fun checkLocationPermission() {
        if (view?.hasLocationPermission() == true) {
            startLocationUpdates()
        }
    }

    fun onQueryChanged(query: String?) {
        this.query = query
        trySearch()
    }

    private fun startLocationUpdates() {
        requestingLocationUpdates = true
        locationClient?.requestLocationUpdates(
            buildLocationRequest(),
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun stopLocationUpdates() {
        if (requestingLocationUpdates) {
            locationClient?.removeLocationUpdates(locationCallback)
        }
    }

    private fun runSearch() = CoroutineScope(Dispatchers.IO).launch {
        try {
            val addresses = addressRepository.searchAddress(query)
            withContext(Dispatchers.Main) { setAddresses(addresses) }
        } catch (e: IOException) {
            withContext(Dispatchers.Main) { view?.showDefaultError() }
        }
    }

    private fun setAddresses(addresses: List<SearchAddress>?) {
        this.addresses.clear()
        if (addresses?.isNotEmpty() == true) {
            this.addresses.addAll(addresses)
        }
        bindAddresses()
    }

    private fun bindAddresses() {
        view?.apply {
            bindAddresses(addresses)
            if (addresses.isEmpty()) {
                if (isValidQuery()) {
                    showEmpty()
                } else {
                    showNoSearch()
                }
            } else {
                showAddresses()
            }
        }
    }

    fun bindQuery() {
        view?.bindQuery(query)
    }

    private fun buildLocationRequest() = LocationRequest.create()?.apply {
        interval = 1000 * 60 //1 minute
        fastestInterval = 1000 * 30 //30 seconds
        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
    }

    fun onClickRetry() {
        trySearch()
    }

    private fun isValidQuery() = query?.length ?: 0 >= 3

    private fun trySearch() {
        if (isValidQuery()) {
            job?.let { if (it.isActive) it.cancel() }
            job = runSearch()
        } else {
            addresses.clear()
            bindAddresses()
        }
    }
}