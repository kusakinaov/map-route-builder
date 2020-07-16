package ku.olga.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.fragment_search_map.view.*
import ku.olga.core_api.AppWithFacade
import ku.olga.ui_core.base.BaseFragment
import javax.inject.Inject

class SearchMapFragment : BaseFragment() {
    @Inject
    lateinit var presenter: SearchMapPresenter
    private var searchMapView: SearchMapView? = null

    override fun inject(activity: FragmentActivity) {
        SearchComponent.build((activity.application as AppWithFacade).getFacade()).inject(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        presenter.locationClient = LocationServices.getFusedLocationProviderClient(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_search_map, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchMapView = SearchMapViewImpl(this, presenter).apply {
            mapView = view.mapView
            onAttach()
        }
    }

    override fun onResume() {
        super.onResume()
        searchMapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        searchMapView?.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchMapView?.onDetach()
    }

    companion object {
        fun newInstance(target: Fragment, requestCode: Int) = SearchMapFragment().apply {
            setTargetFragment(target, requestCode)
        }
    }
}