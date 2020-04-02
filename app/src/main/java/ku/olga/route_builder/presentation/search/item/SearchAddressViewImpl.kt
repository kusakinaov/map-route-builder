package ku.olga.route_builder.presentation.search.item

import android.os.Bundle
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_search_address.view.*
import ku.olga.route_builder.REQ_CODE_EDIT_POINT
import ku.olga.route_builder.presentation.base.BaseFragment
import ku.olga.route_builder.presentation.point.EditPointFragment

class SearchAddressViewImpl(private val fragment: BaseFragment, private val view: View) : SearchAddressView, OnMapReadyCallback {
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
                isScrollGesturesEnabled = true
                isZoomGesturesEnabled = true
                isTiltGesturesEnabled = false
                isRotateGesturesEnabled = false
            }
            setOnMarkerDragListener(object : SimpleMarkerDragListener {
                override fun onMarkerDragEnd(marker: Marker?) {
                    onCoordinatesChanged(marker?.position)
                }
            })
            setOnMapClickListener { onCoordinatesChanged(it) }
        }
        presenter?.bindSearchAddress()
    }

    override fun onCreate(state: Bundle?) {
        view.apply {
            mapView.onCreate(state)
            buttonAdd.setOnClickListener { presenter?.onClickAdd() }
            BottomSheetBehavior.from(layoutContent).setState(BottomSheetBehavior.STATE_EXPANDED)
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
        view.mapView.onDestroy()
    }

    override fun bindLatLng(lat: Double, lon: Double) {
        val position = LatLng(lat, lon)
        if (marker == null) {
            marker = googleMap?.addMarker(MarkerOptions().position(position).draggable(true))
        } else {
            marker?.position = position
        }
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(position, zoomLevel))
    }

    override fun bindAddress(postalAddress: String) {
        view.textViewAddress.text = postalAddress
    }

    override fun editPoint(postalAddress: String, lat: Double, lon: Double) {
        fragment.replaceFragment(EditPointFragment.newInstance(fragment, REQ_CODE_EDIT_POINT, postalAddress, lat, lon))
    }

    override fun onAttach() {
        presenter?.attachView(this)
    }

    override fun onDetach() {
        presenter?.detachView()
    }

    private fun onCoordinatesChanged(latLng: LatLng?) {
        latLng?.let { presenter?.onCoordinatesChanged(it.latitude, it.longitude) }
    }

    interface SimpleMarkerDragListener : GoogleMap.OnMarkerDragListener {
        override fun onMarkerDragStart(p0: Marker?) {
        }

        override fun onMarkerDrag(p0: Marker?) {
        }
    }
}