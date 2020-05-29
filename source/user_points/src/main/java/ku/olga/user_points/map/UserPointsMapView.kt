package ku.olga.user_points.map

import ku.olga.core_api.dto.UserPoint
import ku.olga.ui_core.base.BaseView

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