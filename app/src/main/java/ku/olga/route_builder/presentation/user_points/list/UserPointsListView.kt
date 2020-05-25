package ku.olga.route_builder.presentation.user_points.list

import ku.olga.route_builder.domain.model.UserPoint
import ku.olga.route_builder.presentation.base.BaseView

interface UserPointsListView : BaseView {
    fun setUserPoints(userPoints: List<UserPoint>)
    fun showEmpty()
    fun showUserPoints()
}