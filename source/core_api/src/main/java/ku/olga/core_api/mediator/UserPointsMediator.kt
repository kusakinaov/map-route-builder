package ku.olga.core_api.mediator

import androidx.fragment.app.FragmentManager

interface UserPointsMediator {
    fun openUserPoints(fragmentManager: FragmentManager, containerId: Int)
}