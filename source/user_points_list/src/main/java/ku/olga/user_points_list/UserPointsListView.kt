package ku.olga.user_points_list

import ku.olga.core_api.dto.UserPoint
import ku.olga.ui_core.base.BaseView

interface UserPointsListView : BaseView {
    fun setUserPoints(userPoints: List<UserPoint>)
    fun showEmpty()
    fun showUserPoints()
    fun invalidateUserPoints()
}