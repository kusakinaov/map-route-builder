package ku.olga.category

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.location.LocationServices
import ku.olga.core_api.AppWithFacade
import ku.olga.core_api.dto.Category
import ku.olga.core_api.dto.UserPoint
import ku.olga.core_api.mediator.EditPointMediator
import ku.olga.ui_core.REQ_CODE_EDIT_POINT
import ku.olga.ui_core.REQ_CODE_LOCATION_PERMISSION
import ku.olga.ui_core.base.BaseFragment
import org.osmdroid.config.Configuration
import javax.inject.Inject

class CategoryFragment : BaseFragment() {
    private var categoryView: CategoryView? = null

    @Inject
    lateinit var categoryPresenter: CategoryPresenter

    @Inject
    lateinit var editPointMediator: EditPointMediator

    @Inject
    lateinit var preferences: SharedPreferences

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Configuration.getInstance().load(context, preferences)
        categoryPresenter.apply {
            category = arguments?.getSerializable(CATEGORY) as Category?
            locationClient = LocationServices.getFusedLocationProviderClient(context)
        }
    }

    override fun inject(activity: FragmentActivity) {
        CategoryComponent.build((activity.application as AppWithFacade).getFacade()).inject(this)
    }

    override fun getTitle(resources: Resources) = categoryPresenter.getTitle()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.fragment_category, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryView = object : CategoryViewImpl(view, categoryPresenter) {
            override fun openEditPOI(userPoint: UserPoint) {
                editPoint(userPoint)
            }

            override fun requestLocationPermission() {
                this@CategoryFragment.requestLocationPermission()
            }
        }
        categoryView?.onAttach()
    }

    override fun onResume() {
        super.onResume()
        categoryView?.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.categories, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val view = menu.findItem(R.id.actionSearch)?.actionView
        if (view is SearchView) {
            categoryView?.setSearchView(view)
        }
    }

    override fun onPause() {
        super.onPause()
        categoryView?.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        categoryView?.onDetach()
    }

    override fun isPressBackConsumed(): Boolean = categoryView?.hidePOIDetails() ?: false

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        categoryPresenter.checkLocationPermission()
    }

    private fun requestLocationPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQ_CODE_LOCATION_PERMISSION
        )
    }

    private fun editPoint(userPoint: UserPoint) {
        editPointMediator.editPoint(this, REQ_CODE_EDIT_POINT, userPoint)
    }

    companion object {
        private const val CATEGORY = "category"
        fun newInstance(target: Fragment, requestCode: Int, category: Category) =
            CategoryFragment().apply {
                setTargetFragment(target, requestCode)
                arguments = Bundle().apply { putSerializable(CATEGORY, category) }
            }
    }
}