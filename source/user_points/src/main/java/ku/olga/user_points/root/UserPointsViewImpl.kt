package ku.olga.user_points.root

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_user_points.view.*
import kotlinx.android.synthetic.main.fragment_user_points.view.viewPager
import ku.olga.core_api.dto.UserPoint
import ku.olga.user_points_list.OnUserPointsChangeListener
import ku.olga.user_points.R

abstract class UserPointsViewImpl(
    private val view: View,
    private val tabLayout: TabLayout?,
    private val presenter: UserPointsPresenter,
    private val userPointsAdapter: UserPointsAdapter
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
        view.apply {
            viewPager.apply {
                adapter = userPointsAdapter
                addOnPageChangeListener(pageChangeListener)
                setOnTouchListener { _, _ -> false }
            }
            buttonAdd.apply {
                setOnClickListener { openSearchMap() }
                show()
            }
        }
    }

    override fun bindUserPoints(userPoints: List<UserPoint>) {
        getChildFragment(currentItem).let {
            if (it is ku.olga.user_points_list.OnUserPointsChangeListener) {
                it.onUserPointsChanged(userPoints)
            }
        }
    }

    abstract fun getChildFragment(position: Int): Fragment?

    private val currentItem: Int
        get() = view.viewPager.currentItem

    override fun onAttach() {
        tabLayout?.apply {
            addTab(newTab().setText(R.string.tab_user_points_list), UserPointsAdapter.LIST)
            addTab(newTab().setText(R.string.tab_user_points_map), UserPointsAdapter.MAP)
            visibility = View.VISIBLE
            view.viewPager?.let { setupWithViewPager(it) }
        }
        presenter.attachView(this)
    }

    override fun onDetach() {
        presenter.detachView()
        tabLayout?.apply {
            visibility = View.GONE
            removeAllTabs()
            setupWithViewPager(null)
        }
    }

    override fun onShown() {
        view.buttonAdd?.hide()
    }

    override fun onHide() {
        view.buttonAdd?.show()
    }

    abstract fun openSearchMap()
}