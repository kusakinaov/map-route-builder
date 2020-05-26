package ku.olga.route_builder.presentation.user_points

import ku.olga.core_api.dto.UserPoint

interface OnUserPointsChangeListener {
    fun onUserPointsChanged(userPoints: List<UserPoint>)
}