package ku.olga.route_builder.presentation.user_points.root

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_user_points.*
import ku.olga.route_builder.R
import ku.olga.route_builder.presentation.base.BaseFragment

class UserPointsFragment : BaseFragment() {
    private val tabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {}

        override fun onTabUnselected(tab: TabLayout.Tab?) {}

        override fun onTabSelected(tab: TabLayout.Tab?) {
//            tab?.let {
//                viewPager.currentItem = it.position
//            }
        }
    }
    private lateinit var userPointsAdapter: UserPointsAdapter

    override fun getTitle(resources: Resources) = resources.getString(R.string.ttl_user_points)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        userPointsAdapter = UserPointsAdapter(childFragmentManager, resources)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_user_points, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.findViewById<TabLayout>(R.id.tabLayout)?.apply {
            addTab(newTab().setText(R.string.tab_user_points_list), UserPointsAdapter.LIST)
            addTab(newTab().setText(R.string.tab_user_points_map), UserPointsAdapter.MAP)
            addOnTabSelectedListener(tabSelectedListener)
            visibility = View.VISIBLE
            setupWithViewPager(viewPager)
        }
        viewPager.apply {
            adapter = userPointsAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.findViewById<TabLayout>(R.id.tabLayout)?.apply {
            visibility = View.GONE
            removeAllTabs()
            removeOnTabSelectedListener(tabSelectedListener)
            setupWithViewPager(null)
        }
    }

    companion object {
        fun newInstance() = UserPointsFragment()
    }
}