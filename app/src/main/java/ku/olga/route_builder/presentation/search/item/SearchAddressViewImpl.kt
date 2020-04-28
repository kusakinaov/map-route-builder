package ku.olga.route_builder.presentation.search.item

import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_search_address.view.*
import ku.olga.route_builder.R
import ku.olga.route_builder.REQ_CODE_EDIT_POINT
import ku.olga.route_builder.presentation.base.BaseFragment
import ku.olga.route_builder.presentation.point.EditPointFragment

class SearchAddressViewImpl(private val fragment: BaseFragment) : SearchAddressView, OnMapReadyCallback {
    private val zoomLevel = 15f

    private var googleMap: GoogleMap? = null
    private var marker: Marker? = null

    var presenter: SearchAddressPresenter? = null

    init {
        fragment.view?.mapView?.getMapAsync(this)
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
        fragment.view?.apply {
            mapView.onCreate(state)
            buttonAdd.setOnClickListener { presenter?.onClickAdd() }
            BottomSheetBehavior.from(layoutContent).setState(BottomSheetBehavior.STATE_EXPANDED)
        }
    }

    override fun onStart() {
        fragment.view?.mapView?.onStart()
    }

    override fun onResume() {
        fragment.view?.mapView?.onResume()
    }

    override fun onPause() {
        fragment.view?.mapView?.onPause()
    }

    override fun onStop() {
        fragment.view?.mapView?.onStop()
    }

    override fun onDestroy() {
        fragment.view?.mapView?.onDestroy()
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
        fragment.view?.textViewAddress?.text = postalAddress
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
        presenter?.onCoordinatesChanged(latLng?.latitude, latLng?.longitude)
    }

    override fun showDefaultError() {
        fragment.view?.let {
            Snackbar.make(it, it.resources.getString(R.string.error_search_points), Snackbar.LENGTH_LONG)
                .setAction(R.string.button_retry) {
                    googleMap?.cameraPosition?.target.let {
                        presenter?.onClickRetry(it?.latitude, it?.longitude)
                    }
                }
                .show()
        }
    }

    interface SimpleMarkerDragListener : GoogleMap.OnMarkerDragListener {
        override fun onMarkerDragStart(p0: Marker?) {
        }

        override fun onMarkerDrag(p0: Marker?) {
        }
    }
}