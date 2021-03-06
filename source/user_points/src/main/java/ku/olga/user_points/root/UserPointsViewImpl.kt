package ku.olga.user_points.root

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_user_points.*
import kotlinx.android.synthetic.main.fragment_user_points.view.*
import kotlinx.android.synthetic.main.fragment_user_points.view.viewPager
import ku.olga.ui_core.REQ_CODE_SEARCH_POINT
import ku.olga.core_api.dto.UserPoint
import ku.olga.core_api.mediator.SearchMediator
import ku.olga.ui_core.base.BaseFragment
import ku.olga.user_points.OnUserPointsChangeListener
import ku.olga.user_points.R

class UserPointsViewImpl(
    private val fragment: BaseFragment,
    private val presenter: UserPointsPresenter,
    private val userPointsAdapter: UserPointsAdapter,
    private val searchMediator: SearchMediator
) : UserPointsView {
    private val pageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            presenter.bindUserPoints()
        }
    }

    init {
        fragment.view?.apply {
            viewPager.apply {
                adapter = userPointsAdapter
                addOnPageChangeListener(pageChangeListener)
                setOnTouchListener { _, _ -> false }
            }
            buttonAdd.apply {
                setOnClickListener {
                    searchMediator.openSearchMap(fragment, REQ_CODE_SEARCH_POINT)
                }
                show()
            }
        }
    }

    override fun isPressBackConsumed() = when (val childFragment = getChildFragment()) {
        is BaseFragment -> childFragment.isPressBackConsumed()
        else -> false
    }

    override fun bindUserPoints(userPoints: List<UserPoint>) {
        getChildFragment().let {
            if (it is OnUserPointsChangeListener) {
                it.onUserPointsChanged(userPoints)
            }
        }
    }

    private fun getChildFragment(): Fragment? {
        val fragments = fragment.childFragmentManager.fragments
        val currentPosition = fragment.viewPager?.currentItem ?: 0
        return if (fragments.size > currentPosition) fragments[currentPosition] else null
    }

    override fun onAttach() {
        fragment.activity?.findViewById<TabLayout>(R.id.tabLayout)?.apply {
            addTab(newTab().setText(R.string.tab_user_points_list), UserPointsAdapter.LIST)
            addTab(newTab().setText(R.string.tab_user_points_map), UserPointsAdapter.MAP)
            visibility = View.VISIBLE
            fragment.view?.viewPager?.let { setupWithViewPager(it) }
        }
        presenter.attachView(this)
    }

    override fun onDetach() {
        presenter.detachView()
        fragment.activity?.findViewById<TabLayout>(R.id.tabLayout)?.apply {
            visibility = View.GONE
            removeAllTabs()
            setupWithViewPager(null)
        }
    }

    override fun onShown() {
        fragment.buttonAdd?.hide()
    }

    override fun onHide() {
        fragment.buttonAdd?.show()
    }
}