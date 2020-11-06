package ku.olga.search

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.location.LocationServices
import ku.olga.core_api.AppWithFacade
import ku.olga.core_api.dto.UserPoint
import ku.olga.core_api.mediator.EditPointMediator
import ku.olga.ui_core.REQ_CODE_EDIT_POINT
import ku.olga.ui_core.REQ_CODE_LOCATION_PERMISSION
import ku.olga.ui_core.base.BaseFragment
import ku.olga.ui_core.utils.hideKeyboard
import javax.inject.Inject

class SearchMapFragment : BaseFragment(R.layout.fragment_search_map) {
    @Inject
    lateinit var presenter: SearchMapPresenter

    @Inject
    lateinit var editPointMediator: EditPointMediator

    private var searchMapView: SearchMapView? = null

    override fun inject(activity: FragmentActivity) {
        SearchComponent.build((activity.application as AppWithFacade).getFacade()).inject(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        presenter.locationClient = LocationServices.getFusedLocationProviderClient(context)
        activity?.onBackPressedDispatcher?.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (searchMapView?.closeBottomSheet() != true) {
                    isEnabled = false
                    popBackStack()
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchMapView = buildSearchMapView(view).apply { onAttach() }
    }

    private fun buildSearchMapView(view: View) = object : SearchMapViewImpl(view, presenter) {
        override fun hideKeyboard() {
            this@SearchMapFragment.hideKeyboard()
        }

        override fun showEditDialog(userPoint: UserPoint) {
            editPointMediator.editPoint(this@SearchMapFragment, REQ_CODE_EDIT_POINT, userPoint)
        }

        override fun requestLocationPermission() {
            ku.olga.ui_core.utils.requestLocationPermission(
                this@SearchMapFragment,
                REQ_CODE_LOCATION_PERMISSION
            )
        }

    }

    override fun onResume() {
        super.onResume()
        searchMapView?.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.map_search, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        searchMapView?.searchView = menu.findItem(R.id.actionSearch).actionView as SearchView
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