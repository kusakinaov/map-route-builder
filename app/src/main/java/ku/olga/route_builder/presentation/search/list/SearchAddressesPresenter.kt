package ku.olga.route_builder.presentation.search.list

import android.location.Location
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.*
import ku.olga.route_builder.domain.model.Category
import ku.olga.route_builder.domain.model.SearchAddress
import ku.olga.route_builder.domain.repository.AddressRepository
import ku.olga.route_builder.domain.repository.CategoryRepository
import ku.olga.route_builder.presentation.base.BasePresenter
import java.io.IOException

class SearchAddressesPresenter(
    private val addressRepository: AddressRepository,
    private val categoryRepository: CategoryRepository
) : BasePresenter<SearchAddressesView>() {
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
    private val categories = mutableListOf<Category>()

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
        bindData()

        if (!isValidQuery() && categories.isEmpty()) loadCategories()
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
        view?.let {
            this.query = query
            trySearch()
        }
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

    private fun loadCategories() = CoroutineScope(Dispatchers.IO).launch {
        try {
            withContext(Dispatchers.Main) { showProgress() }
            val categories = categoryRepository.getCategories(query)
            withContext(Dispatchers.Main) { setCategories(categories) }
        } catch (e: IOException) {
            withContext(Dispatchers.Main) { view?.showDefaultError() }
        }
    }

    private fun setCategories(categories: List<Category>) {
        this.categories.clear()
        this.categories.addAll(categories)
        bindData()
    }

    private fun setAddresses(addresses: List<SearchAddress>?) {
        this.addresses.clear()
        if (addresses?.isNotEmpty() == true) {
            this.addresses.addAll(addresses)
        }
        bindData()
    }

    private fun bindData() {
        view?.apply {
            bindAddresses(addresses)
            bindCategories(categories)
            if (isValidQuery()) {
                if (addresses.isEmpty()) {
                    showEmpty()
                } else {
                    showAddresses()
                }
            } else {
                showCategories()
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
            bindData()
        }
    }
}