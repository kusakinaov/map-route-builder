package ku.olga.route_builder.presentation.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_user_points_map.*
import ku.olga.route_builder.domain.model.UserPoint

class UserPointsMapViewImpl(val fragment: Fragment, private val presenter: UserPointsMapPresenter) : UserPointsMapView,
        OnMapReadyCallback {
    private var googleMap: GoogleMap? = null
    private val markers = mutableListOf<Marker>()
    private var markersBindSuccessful = false

    override fun onAttach() {
        presenter.attachView(this)
    }

    override fun onAttach(bundle: Bundle?) {
        onAttach()
        fragment.mapView?.apply {
            getMapAsync(this@UserPointsMapViewImpl)
            onCreate(bundle)
        }
    }

    override fun onStart() {
        fragment.mapView?.onStart()
    }

    override fun onResume() {
        fragment.mapView?.onResume()
    }

    override fun onPause() {
        fragment.mapView?.onPause()
    }

    override fun onStop() {
        fragment.mapView?.onStop()
    }

    override fun onDetach() {
        markersBindSuccessful = false
        fragment.mapView?.onDestroy()
        presenter.detachView()
    }

    override fun setUserPoints(userPoints: List<UserPoint>) {
        googleMap?.let { map ->
            for (marker in markers) {
                marker.remove()
            }
            markers.clear()

            var latLng: LatLng? = null
            var bounds: LatLngBounds? = null
            for (userPoint in userPoints) {
                if (latLng == null) {
                    latLng = LatLng(userPoint.lat, userPoint.lon)
                } else {
                    bounds = LatLngBounds(latLng, LatLng(userPoint.lat, userPoint.lon).also {
                        latLng = it
                    })
                }

                map.addMarker(MarkerOptions()
                        .position(latLng!!)
                        .title(userPoint.title)
                )?.also { markers.add(it) }
            }
            if (bounds != null) {
                map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 16))
            } else if (latLng != null) {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            }

            markersBindSuccessful = true
        }
    }

    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0
        googleMap?.apply {
            uiSettings.apply {
                isScrollGesturesEnabled = true
                isZoomGesturesEnabled = true
                isTiltGesturesEnabled = false
                isRotateGesturesEnabled = false
            }
        }
        if (!markersBindSuccessful) presenter.bindUserPoints()
    }
}
