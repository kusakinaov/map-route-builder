package ku.olga.route_builder.presentation.search

import android.os.Bundle
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_search_address.view.*

class SearchAddressViewImpl(private val fragment: SearchAddressFragment, private val view: View) : SearchAddressView, OnMapReadyCallback {
    private val zoomLevel = 15f

    private var googleMap: GoogleMap? = null
    private var marker: Marker? = null

    var presenter: SearchAddressPresenter? = null

    init {
        view.mapView.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0
        googleMap?.apply {
            uiSettings.apply {
                isScrollGesturesEnabled = false
                isZoomGesturesEnabled = true
                isTiltGesturesEnabled = false
                isRotateGesturesEnabled = false
            }
            setOnMarkerClickListener {
                it?.apply { if (isInfoWindowShown) hideInfoWindow() else showInfoWindow() }
                true
            }
        }
        presenter?.bindSearchAddress()
    }

    override fun onCreate(state: Bundle?) {
        presenter?.attachView(this)
        view.apply {
            mapView.onCreate(state)
            buttonAdd.setOnClickListener { presenter?.onClickAdd() }
        }
    }

    override fun onStart() {
        view.mapView.onStart()
    }

    override fun onResume() {
        view.mapView.onResume()
    }

    override fun onPause() {
        view.mapView.onPause()
    }

    override fun onStop() {
        view.mapView.onStop()
    }

    override fun onDestroy() {
        presenter?.detachView()
        view.mapView.onDestroy()
    }

    override fun bindLatLng(lat: Double, lon: Double) {
        val position = LatLng(lat, lon)
        googleMap?.apply {
            marker = addMarker(MarkerOptions().position(position))
            moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoomLevel))
        }
    }

    override fun bindAddress(postalAddress: String) {
        fragment.activity?.title = postalAddress
        marker?.title = postalAddress
    }
}