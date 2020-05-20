package ku.olga.route_builder.presentation.user_points.map

import ku.olga.route_builder.domain.model.UserPoint
import ku.olga.route_builder.presentation.base.BaseView

interface UserPointsMapView : BaseView {
    fun setUserPoints(userPoints: List<UserPoint>)
    fun moveTo(userPoints: List<UserPoint>, animated: Boolean)
    fun moveTo(userPoint: UserPoint, animated: Boolean)
    fun onResume()
    fun onPause()
    fun hideUserPoint(): Boolean
    fun editUserPoint(userPoint: UserPoint)
    fun showUserPoint(userPoint: UserPoint)
}