package ku.olga.route_builder.presentation.user_points.root

import android.view.View
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_user_points.*
import kotlinx.android.synthetic.main.fragment_user_points.view.*
import kotlinx.android.synthetic.main.fragment_user_points.view.viewPager
import ku.olga.route_builder.R
import ku.olga.route_builder.REQ_CODE_SEARCH_POINT
import ku.olga.route_builder.presentation.base.BaseFragment
import ku.olga.route_builder.presentation.search.list.SearchAddressesFragment

class UserPointsViewImpl(val fragment: UserPointsFragment, private val userPointsAdapter: UserPointsAdapter) : UserPointsView {
    init {
        fragment.view?.apply {
            viewPager.apply {
                adapter = userPointsAdapter
            }
            buttonAdd.setOnClickListener {
                fragment.replaceFragment(SearchAddressesFragment
                        .newInstance(fragment, REQ_CODE_SEARCH_POINT))
            }
        }
    }

    override fun isPressBackConsumed(): Boolean {
        val fragments = fragment.childFragmentManager.fragments
        val currentPosition = fragment.viewPager?.currentItem ?: 0
        if (fragments.size > currentPosition) {
            val fragment = fragments[currentPosition]
            if (fragment is BaseFragment) {
                return fragment.isPressBackConsumed()
            }
        }
        return false
    }

    override fun onAttach() {
        fragment.activity?.findViewById<TabLayout>(R.id.tabLayout)?.apply {
            addTab(newTab().setText(R.string.tab_user_points_list), UserPointsAdapter.LIST)
            addTab(newTab().setText(R.string.tab_user_points_map), UserPointsAdapter.MAP)
            visibility = View.VISIBLE
            fragment.view?.viewPager?.let { setupWithViewPager(it) }
        }
    }

    override fun onDetach() {
        fragment.activity?.findViewById<TabLayout>(R.id.tabLayout)?.apply {
            visibility = View.GONE
            removeAllTabs()
            setupWithViewPager(null)
        }
    }
}