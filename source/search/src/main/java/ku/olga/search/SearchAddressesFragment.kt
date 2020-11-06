package ku.olga.search

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.location.LocationServices
import ku.olga.core_api.AppWithFacade
import ku.olga.ui_core.REQ_CODE_VIEW_CATEGORY
import ku.olga.ui_core.REQ_CODE_VIEW_SEARCH_ADDRESS
import ku.olga.core_api.dto.Category
import ku.olga.core_api.dto.SearchAddress
import ku.olga.core_api.dto.UserPoint
import ku.olga.core_api.dto.UserPointType
import ku.olga.core_api.mediator.CategoryMediator
import ku.olga.core_api.mediator.EditPointMediator
import ku.olga.ui_core.REQ_CODE_LOCATION_PERMISSION
import ku.olga.ui_core.base.BaseFragment
import ku.olga.ui_core.utils.hideKeyboard
import javax.inject.Inject

class SearchAddressesFragment : BaseFragment(R.layout.fragment_search) {
    @Inject
    lateinit var searchPresenter: SearchAddressesPresenter

    @Inject
    lateinit var searchAdapter: AddressesAdapter

    @Inject
    lateinit var categoriesAdapter: CategoriesAdapter

    @Inject
    lateinit var categoryMediator: CategoryMediator

    @Inject
    lateinit var editPointMediator: EditPointMediator

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
        SearchComponent.build((activity.application as AppWithFacade).getFacade()).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchAddressesView = buildSearchAddressView(view).apply { onAttach() }
    }

    private fun buildSearchAddressView(view: View) =
        object : SearchAddressesViewImpl(view, searchPresenter, searchAdapter, categoriesAdapter) {
            override fun requestLocationPermission() {
                ku.olga.ui_core.utils.requestLocationPermission(
                    this@SearchAddressesFragment,
                    REQ_CODE_LOCATION_PERMISSION
                )
            }
        }

    private fun openSearchAddress(searchAddress: SearchAddress) {
        hideKeyboard()
        editPointMediator.editPoint(
            this,
            REQ_CODE_VIEW_SEARCH_ADDRESS,
            UserPoint(
                postalAddress = searchAddress.postalAddress,
                lat = searchAddress.lat,
                lon = searchAddress.lon,
                type = UserPointType.ADDRESS
            )
        )
    }

    private fun openCategory(category: Category) {
        hideKeyboard()
        categoryMediator.openCategory(this, REQ_CODE_VIEW_CATEGORY, category)
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
            SearchAddressesFragment()
                .apply { setTargetFragment(target, requestCode) }
    }
}