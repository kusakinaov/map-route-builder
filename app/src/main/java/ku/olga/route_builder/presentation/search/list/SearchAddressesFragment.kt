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
import ku.olga.ui_core.REQ_CODE_VIEW_CATEGORY
import ku.olga.ui_core.REQ_CODE_VIEW_SEARCH_ADDRESS
import ku.olga.core_api.dto.Category
import ku.olga.core_api.dto.SearchAddress
import ku.olga.route_builder.presentation.MainActivity
import ku.olga.ui_core.base.BaseFragment
import ku.olga.ui_core.utils.hideKeyboard
import ku.olga.route_builder.presentation.point.EditPointFragment
import ku.olga.category.CategoryFragment
import javax.inject.Inject

class SearchAddressesFragment : BaseFragment() {
    @Inject
    lateinit var searchPresenter: SearchAddressesPresenter

    @Inject
    lateinit var searchAdapter: AddressesAdapter

    @Inject
    lateinit var categoriesAdapter: CategoriesAdapter

    private var searchAddressesView: SearchAddressesView? = null

    override fun getTitle(resources: Resources) = resources.getString(R.string.ttl_search)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        searchAdapter.apply {
            highlightColor = ContextCompat.getColor(context, R.color.secondaryColor)
            onClickAddressListener = { openSearchAddress(it) }
        }
        categoriesAdapter.categoryClickListener = { openCategory(it) }
        searchPresenter.apply {
            locationClient = LocationServices.getFusedLocationProviderClient(context)
        }
    }

    override fun inject(activity: FragmentActivity) {
        if (activity is MainActivity) {
            activity.getActivityComponent()?.inject(this)
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
        replaceFragment(
            ku.olga.category.CategoryFragment.newInstance(this,
            REQ_CODE_VIEW_CATEGORY, category), true)
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