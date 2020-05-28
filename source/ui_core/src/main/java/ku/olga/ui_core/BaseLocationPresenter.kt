package ku.olga.ui_core

import android.content.SharedPreferences
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import ku.olga.core_api.dto.Coordinates

open class BaseLocationPresenter<T : BaseLocationView>(val preferences: SharedPreferences) :
    BasePresenter<T>() {
    var locationClient: FusedLocationProviderClient? = null
    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return

            for (l in locationResult.locations) {
                onCoordinatesChanged(
                    Coordinates(
                        l.latitude,
                        l.longitude
                    )
                )
            }
        }
    }
    private var coordinates: Coordinates? = null
    private var requestingLocationUpdates: Boolean = false

    override fun attachView(view: T) {
        super.attachView(view)
        if (!view.hasLocationPermission()) {
            view.requestLocationPermission()
        }
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

    private fun buildLocationRequest() = LocationRequest.create()?.apply {
        interval = 1000 * 60 //1 minute
        fastestInterval = 1000 * 30 //30 seconds
        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
    }

    fun onCoordinatesChanged(coordinates: Coordinates) {
        this.coordinates = coordinates
        setLastCoordinates(preferences, coordinates)
    }
}