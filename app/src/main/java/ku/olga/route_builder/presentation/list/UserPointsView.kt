package ku.olga.route_builder.presentation.list

import ku.olga.route_builder.domain.model.UserPoint
import ku.olga.route_builder.presentation.base.BaseView

interface UserPointsView : BaseView {
    fun setUserPoints(userPoints: List<UserPoint>)
    fun onClickOpenMap()
    fun showEmpty()
    fun showUserPoints()
}