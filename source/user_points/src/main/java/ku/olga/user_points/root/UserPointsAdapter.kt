package ku.olga.user_points.root

import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import ku.olga.user_points.R
import ku.olga.user_points.map.UserPointsMapFragment
import ku.olga.user_points_list.UserPointsListFragment
import java.lang.IllegalArgumentException

class UserPointsAdapter(fm: FragmentManager, private val resources: Resources) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment = when (position) {
        LIST -> UserPointsListFragment.newInstance()
        MAP -> UserPointsMapFragment.newInstance()
        else -> throw IllegalArgumentException()
    }

    override fun getPageTitle(position: Int): CharSequence? = when (position) {
        LIST -> resources.getString(R.string.tab_user_points_list)
        MAP -> resources.getString(R.string.tab_user_points_map)
        else -> ""
    }

    override fun getCount() = 2

    companion object {
        const val LIST = 0
        const val MAP = 1
    }
}