package ku.olga.route_builder.presentation.search

import android.location.Location
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.*
import ku.olga.route_builder.domain.model.SearchAddress
import ku.olga.route_builder.domain.repository.PointsRepository
import ku.olga.route_builder.presentation.base.BasePresenter

class SearchAddressesPresenter(private val pointsRepository: PointsRepository) : BasePresenter<SearchAddressesView>() {
    var locationClient: FusedLocationProviderClient? = null

    var query: String? = null
        set(value) {
            field = value
            if (value?.length ?: 0 >= 3) {
                job?.let { if (it.isActive) it.cancel() }
                job = runSearch()
            }
        }

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
    var job: Job? = null

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

    private fun startLocationUpdates() {
        requestingLocationUpdates = true
        locationClient?.requestLocationUpdates(buildLocationRequest(), locationCallback, Looper.getMainLooper())
    }

    private fun stopLocationUpdates() {
        if (requestingLocationUpdates) {
            locationClient?.removeLocationUpdates(locationCallback)
        }
    }

    private fun runSearch() = CoroutineScope(Dispatchers.IO).launch {
        val addresses = pointsRepository.searchAddress(query)
        withContext(Dispatchers.Main) { setAddresses(addresses) }
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
            if (addresses.isEmpty()) {
                showEmpty()
            } else {
                showAddresses(addresses)
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
}