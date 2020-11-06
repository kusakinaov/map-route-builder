package ku.olga.user_points.root

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.tabs.TabLayout
import ku.olga.core_api.AppWithFacade
import ku.olga.core_api.mediator.SearchMediator
import ku.olga.ui_core.REQ_CODE_SEARCH_POINT
import ku.olga.ui_core.base.BaseFragment
import ku.olga.user_points.R
import ku.olga.user_points.list.UserPointsListFragment
import ku.olga.user_points.map.UserPointsMapFragment
import javax.inject.Inject

class UserPointsFragment : BaseFragment(R.layout.fragment_user_points),
    UserPointsMapFragment.BottomSheetCallback, UserPointsListFragment.OnOrderChangeCallback {
    @Inject
    lateinit var presenter: UserPointsPresenter

    @Inject
    lateinit var searchMediator: SearchMediator

    private lateinit var userPointsAdapter: UserPointsAdapter
    private var userPointsView: UserPointsView? = null

    override fun getTitle(resources: Resources) = resources.getString(R.string.ttl_user_points)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        userPointsAdapter = UserPointsAdapter(
            childFragmentManager,
            resources
        )
    }

    override fun inject(activity: FragmentActivity) {
        UserPointsComponent.build((activity.application as AppWithFacade).getFacade()).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userPointsView = buildUserPointsView(view)
        userPointsView?.onAttach()
    }

    private fun buildUserPointsView(view: View) = object : UserPointsViewImpl(
        view,
        activity?.findViewById(R.id.tabLayout) as TabLayout?,
        presenter,
        userPointsAdapter
    ) {
        override fun getChildFragment(position: Int): Fragment? =
            this@UserPointsFragment.getChildFragment(position)

        override fun openSearchMap() {
            this@UserPointsFragment.openSearchMap()
        }
    }

    private fun getChildFragment(position: Int) = childFragmentManager.fragments.run {
        if (size > position) get(position) else null
    }

    private fun openSearchMap() {
        searchMediator.openSearchMap(this@UserPointsFragment, REQ_CODE_SEARCH_POINT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        userPointsView?.onDetach()
    }

    companion object {
        fun newInstance() = UserPointsFragment()
    }

    override fun onShown() {
        userPointsView?.onShown()
    }

    override fun onHide() {
        userPointsView?.onHide()
    }

    override fun onOrderChanged() {
        presenter.invalidateUserPoints()
    }
}