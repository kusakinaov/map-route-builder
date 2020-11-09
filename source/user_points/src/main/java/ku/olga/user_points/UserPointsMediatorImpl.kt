package ku.olga.user_points

import androidx.fragment.app.FragmentManager
import ku.olga.core_api.mediator.UserPointsMediator
import javax.inject.Inject

class UserPointsMediatorImpl @Inject constructor() : UserPointsMediator {
    override fun openUserPoints(fragmentManager: FragmentManager, containerId: Int) {
        fragmentManager.beginTransaction()
            .replace(containerId, UserPointsFragment.newInstance()).commit()
    }
}