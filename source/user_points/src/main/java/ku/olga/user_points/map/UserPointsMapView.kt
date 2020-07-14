package ku.olga.user_points.map

import ku.olga.core_api.dto.Coordinates
import ku.olga.core_api.dto.UserPoint
import ku.olga.ui_core.base.BaseMapView

interface UserPointsMapView : BaseMapView {
    fun setUserPoints(userPoints: List<UserPoint>)
    fun hideUserPoint(): Boolean
    fun editUserPoint(userPoint: UserPoint)
    fun showUserPoint(userPoint: UserPoint)

    fun showDirectionsError()
    fun showDirections(coordinates: List<Coordinates>)
}