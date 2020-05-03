package ku.olga.route_builder.presentation.map

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_user_points_map.*
import kotlinx.android.synthetic.main.fragment_user_points_map.view.*
import ku.olga.route_builder.REQ_CODE_EDIT_POINT
import ku.olga.route_builder.domain.model.UserPoint
import ku.olga.route_builder.presentation.base.BaseFragment
import ku.olga.route_builder.presentation.point.EditPointFragment

class UserPointsMapViewImpl(val fragment: Fragment, private val presenter: UserPointsMapPresenter) : UserPointsMapView,
        OnMapReadyCallback {
    private var googleMap: GoogleMap? = null
    private var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>? = null
    private val markers = mutableListOf<Marker>()
    private var markersBindSuccessful = false

    override fun onAttach() {
        presenter.attachView(this)
    }

    override fun onAttach(bundle: Bundle?) {
        onAttach()
        fragment.view?.let {
            it.mapView?.apply {
                getMapAsync(this@UserPointsMapViewImpl)
                onCreate(bundle)
            }
            it.buttonEdit.setOnClickListener {
                if (it.tag is UserPoint) {
                    presenter.onClickEditUserPoint(it.tag as UserPoint)
                }
            }
            bottomSheetBehavior = BottomSheetBehavior.from(it.layoutContent).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }
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

    override fun hideBottomSheet(): Boolean {
        val expanded = bottomSheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED
        if (expanded) {
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
            fragment.view?.buttonEdit?.visibility = View.GONE
        }
        return expanded
    }

    override fun editUserPoint(userPoint: UserPoint) {
        if (fragment is BaseFragment) {
            fragment.replaceFragment(EditPointFragment
                    .newInstance(fragment, REQ_CODE_EDIT_POINT, userPoint), true)
        }
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
            setOnMarkerClickListener {
                showDetails(it)
                true
            }
        }
        if (!markersBindSuccessful) presenter.bindUserPoints()
    }

    private fun showDetails(marker: Marker) {
        googleMap?.apply {
            animateCamera(CameraUpdateFactory.newLatLngZoom(marker.position, cameraPosition.zoom))
        }
        presenter.getUserPointAt(markers.indexOf(marker)).let {
            fragment.view?.apply {
                textViewTitle.text = it.title
                textViewDescription.apply {
                    text = it.description
                    visibility = if (it.description.isNullOrEmpty()) View.GONE else View.VISIBLE
                }
                textViewAddress.text = it.postalAddress
                buttonEdit.apply {
                    tag = it
                    visibility = View.VISIBLE
                }
            }
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }
}
