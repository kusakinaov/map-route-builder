package ku.olga.route_builder.presentation.search.list

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.location.LocationServices
import ku.olga.route_builder.R
import ku.olga.route_builder.REQ_CODE_VIEW_SEARCH_ADDRESS
import ku.olga.route_builder.domain.model.SearchAddress
import ku.olga.route_builder.presentation.MainActivity
import ku.olga.route_builder.presentation.base.BaseFragment
import ku.olga.route_builder.presentation.hideKeyboard
import ku.olga.route_builder.presentation.search.item.SearchAddressFragment
import javax.inject.Inject

class SearchAddressesFragment : BaseFragment() {
    @Inject
    lateinit var searchPresenter: SearchAddressesPresenter

    @Inject
    lateinit var searchAdapter:SearchAddressAdapter

    private var searchAddressesView: SearchAddressesView? = null

    override fun getTitle(resources: Resources) = resources.getString(R.string.ttl_search)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        searchAdapter.highlightColor = ContextCompat.getColor(context, R.color.secondaryColor)
        searchPresenter.apply {
            locationClient = LocationServices.getFusedLocationProviderClient(context)
        }
    }

    override fun inject(activity: FragmentActivity) {
        if (activity is MainActivity) {
            activity.getActivityComponent()?.inject(this)
        }
        searchAdapter.apply { onClickAddressListener = { openSearchAddress(it) } }
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
            searchAdapter
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
        searchPresenter.onQueryChanged(query)
        searchAdapter.setQuery(query)
    }

    companion object {
        fun newInstance(target: Fragment, requestCode: Int) =
            SearchAddressesFragment().apply { setTargetFragment(target, requestCode) }
    }
}