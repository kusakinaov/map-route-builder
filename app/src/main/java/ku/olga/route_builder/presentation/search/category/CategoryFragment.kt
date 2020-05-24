package ku.olga.route_builder.presentation.search.category

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.location.LocationServices
import ku.olga.route_builder.R
import ku.olga.route_builder.domain.model.Category
import ku.olga.route_builder.presentation.App
import ku.olga.route_builder.presentation.base.BaseFragment

class CategoryFragment : BaseFragment() {
    private var categoryView: CategoryView? = null
    private val categoryPresenter = CategoryPresenter(App.poiRepository)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        categoryPresenter.apply {
            category = arguments?.getSerializable(CATEGORY) as Category?
            locationClient = LocationServices.getFusedLocationProviderClient(context)
        }
    }

    override fun getTitle(resources: Resources) = categoryPresenter.getTitle()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_category, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryView = CategoryViewImpl(this, categoryPresenter)
        categoryView?.onAttach()
    }

    override fun onResume() {
        super.onResume()
        categoryView?.onResume()
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

    companion object {
        private const val CATEGORY = "category"
        fun newInstance(target: Fragment, requestCode: Int, category: Category) =
                CategoryFragment().apply {
                    setTargetFragment(target, requestCode)
                    arguments = Bundle().apply { putSerializable(CATEGORY, category) }
                }
    }
}