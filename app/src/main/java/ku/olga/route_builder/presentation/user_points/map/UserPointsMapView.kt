package ku.olga.route_builder.presentation.user_points.map

import ku.olga.core_api.dto.UserPoint
import ku.olga.route_builder.presentation.base.BaseView

interface UserPointsMapView : BaseView {
    fun setUserPoints(userPoints: List<UserPoint>)
    fun moveTo(latitude: Double, longitude: Double, zoomLevel: Double, animated: Boolean)
    fun moveTo(userPoints: List<UserPoint>, animated: Boolean)
    fun onResume()
    fun onPause()
    fun hideUserPoint(): Boolean
    fun editUserPoint(userPoint: UserPoint)
    fun showUserPoint(userPoint: UserPoint)
}