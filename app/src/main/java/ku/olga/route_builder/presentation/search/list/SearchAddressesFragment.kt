package ku.olga.route_builder.presentation.search.list

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.google.android.gms.location.LocationServices
import ku.olga.route_builder.R
import ku.olga.route_builder.REQ_CODE_VIEW_SEARCH_ADDRESS
import ku.olga.route_builder.domain.model.SearchAddress
import ku.olga.route_builder.presentation.App
import ku.olga.route_builder.presentation.KeyboardUtils.hideKeyboard
import ku.olga.route_builder.presentation.base.BaseFragment
import ku.olga.route_builder.presentation.search.item.SearchAddressFragment

class SearchAddressesFragment : BaseFragment() {
    private val searchPresenter = SearchAddressesPresenter(App.pointsService)
    private var searchAddressesView: SearchAddressesView? = null

    override fun getTitle(resources: Resources) = resources.getString(R.string.ttl_search)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        searchPresenter.apply {
            locationClient = LocationServices.getFusedLocationProviderClient(context)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.fragment_search, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchAddressesView = SearchAddressesViewImpl(
            this,
            searchPresenter,
            SearchAddressAdapter().apply { onClickAddressListener = { openSearchAddress(it) } },
            view
        )
        searchAddressesView?.onAttach()
    }

    private fun openSearchAddress(searchAddress: SearchAddress) {
        hideKeyboard()
        replaceFragment(
            SearchAddressFragment.newInstance(
                this@SearchAddressesFragment,
                REQ_CODE_VIEW_SEARCH_ADDRESS, searchAddress
            ), true
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        searchPresenter.checkLocationPermission()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchAddressesView?.onDetach()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.search_points, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val view = menu.findItem(R.id.actionSearch)?.actionView
        if (view is SearchView) {
            view.apply {
                isIconified = false
                queryHint = getString(R.string.hint_search_points)
                setOnQueryTextListener(buildOnQueryTextListener())
            }
            if (searchAddressesView is SearchAddressesViewImpl) {
                (searchAddressesView as SearchAddressesViewImpl).searchView = view
            }
        }
    }

    private fun buildOnQueryTextListener() = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?) = true

        override fun onQueryTextChange(newText: String?): Boolean {
            onQueryChanged(newText)
            return true
        }
    }

    fun onQueryChanged(query: String?) {
        searchPresenter.query = query
    }

    companion object {
        fun newInstance(target: Fragment, requestCode: Int) =
            SearchAddressesFragment().apply { setTargetFragment(target, requestCode) }
    }
}