package ku.olga.user_points_list

import ku.olga.core_api.dto.UserPoint

interface OnUserPointsChangeListener {
    fun onUserPointsChanged(userPoints: List<UserPoint>)
}