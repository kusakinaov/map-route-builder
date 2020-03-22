package ku.olga.route_builder.presentation.map

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import ku.olga.route_builder.R
import ku.olga.route_builder.presentation.base.BaseFragment

class MapFragment : BaseFragment(), OnMapReadyCallback {
    private var map: MapView? = null
    private var googleMap: GoogleMap? = null

    override fun getTitle(resources: Resources) = resources.getString(R.string.ttl_map)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        map = MapView(inflater.context)
        return map
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        map?.getMapAsync(this)
        map?.onCreate(savedInstanceState)
    }

    override fun onMapReady(map: GoogleMap?) {
        googleMap = map
    }


    override fun onStart() {
        super.onStart()
        map?.onStart()
    }

    override fun onResume() {
        super.onResume()
        map?.onResume()
    }

    override fun onPause() {
        map?.onPause()
        super.onPause()
    }

    override fun onStop() {
        map?.onStop()
        super.onStop()
    }

    override fun onDestroyView() {
        map?.onDestroy()
        super.onDestroyView()
    }
}