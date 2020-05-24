package ku.olga.route_builder.presentation.search.list

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.LocationServices
import ku.olga.route_builder.R
import ku.olga.route_builder.REQ_CODE_VIEW_CATEGORY
import ku.olga.route_builder.REQ_CODE_VIEW_SEARCH_ADDRESS
import ku.olga.route_builder.domain.model.Category
import ku.olga.route_builder.domain.model.SearchAddress
import ku.olga.route_builder.presentation.App
import ku.olga.route_builder.presentation.base.BaseFragment
import ku.olga.route_builder.presentation.hideKeyboard
import ku.olga.route_builder.presentation.point.EditPointFragment
import ku.olga.route_builder.presentation.search.category.CategoryFragment

class SearchAddressesFragment : BaseFragment() {
    private val searchPresenter =
        SearchAddressesPresenter(App.addressRepository, App.categoriesRepository)
    private var searchAddressesView: SearchAddressesView? = null
    private val searchAdapter =
        AddressesAdapter().apply { onClickAddressListener = { openSearchAddress(it) } }
    private val categoriesAdapter = CategoriesAdapter()
        .apply {
        categoryClickListener = { openCategory(it) }
    }

    override fun getTitle(resources: Resources) = resources.getString(R.string.ttl_search)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        searchAdapter.highlightColor = ContextCompat.getColor(context, R.color.secondaryColor)
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
            searchAdapter,
            categoriesAdapter
        )
        searchAddressesView?.onAttach()
    }

    private fun openSearchAddress(searchAddress: SearchAddress) {
        hideKeyboard()
        replaceFragment(
            EditPointFragment
                .newInstance(
                    this, REQ_CODE_VIEW_SEARCH_ADDRESS,
                    searchAddress.postalAddress, searchAddress.lat, searchAddress.lon
                ),
            true
        )
    }

    private fun openCategory(category: Category) {
        hideKeyboard()
        replaceFragment(CategoryFragment.newInstance(this, REQ_CODE_VIEW_CATEGORY, category), true)
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
            searchAddressesView?.searchView = view
        }
    }

    companion object {
        fun newInstance(target: Fragment, requestCode: Int) =
            SearchAddressesFragment().apply { setTargetFragment(target, requestCode) }
    }
}