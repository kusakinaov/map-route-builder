package ku.olga.route_builder.presentation.user_points.map

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
import ku.olga.route_builder.presentation.convertDpToPx
import ku.olga.route_builder.presentation.point.EditPointFragment
import kotlin.math.max
import kotlin.math.min

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
            bottomSheetBehavior = BottomSheetBehavior.from(it.layoutContent)
                    .apply { state = BottomSheetBehavior.STATE_HIDDEN }
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

    override fun showBottomMenu(userPoint: UserPoint) {
        fragment.view?.apply {
            textViewTitle.text = userPoint.title
            textViewDescription.apply {
                text = userPoint.description
                visibility = if (userPoint.description.isNullOrEmpty()) View.GONE else View.VISIBLE
            }
            textViewAddress.text = userPoint.postalAddress
            buttonEdit.apply {
                tag = userPoint
                visibility = View.VISIBLE
            }
        }
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onDetach() {
        markersBindSuccessful = false
        fragment.mapView?.onDestroy()
        presenter.detachView()
    }

    override fun setUserPoints(userPoints: List<UserPoint>) {
        googleMap?.let { map ->
            for (marker in markers) marker.remove()
            markers.clear()
            for (userPoint in userPoints) {
                map.addMarker(MarkerOptions()
                        .position(userPoint.getLatLng())
                        .title(userPoint.title))?.also { markers.add(it) }
            }
            markersBindSuccessful = true
        }
    }

    override fun animateTo(userPoints: List<UserPoint>) {
        googleMap?.apply {
            if (userPoints.size == 1) {
                animateCamera(CameraUpdateFactory.newLatLngZoom(userPoints[0].getLatLng(),
                        if (markers.size > 1) cameraPosition.zoom else 15f))
            } else if (userPoints.size > 1) {
                buildLatLngBounds(userPoints)?.let {
                    animateCamera(CameraUpdateFactory
                            .newLatLngBounds(it, convertDpToPx(fragment.resources, 24f).toInt()))
                }
            }
        }
    }

    private fun buildLatLngBounds(userPoints: List<UserPoint>): LatLngBounds? {
        var southWest: LatLng? = null
        var northEast: LatLng? = null
        var bounds: LatLngBounds? = null
        for (userPoint in userPoints) {
            if (southWest == null || northEast == null) {
                southWest = userPoint.getLatLng()
                northEast = userPoint.getLatLng()
            } else {
                southWest = LatLng(min(userPoint.lat, southWest.latitude), min(userPoint.lon, southWest.longitude))
                northEast = LatLng(max(userPoint.lat, northEast.latitude), max(userPoint.lon, northEast.longitude))
                bounds = LatLngBounds(southWest, northEast)
            }
        }
        return bounds
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
                presenter.onClickMarker(markers.indexOf(it))
                true
            }
        }
        if (!markersBindSuccessful) presenter.bindUserPoints()
    }

    private fun UserPoint.getLatLng() = LatLng(lat, lon)
}
