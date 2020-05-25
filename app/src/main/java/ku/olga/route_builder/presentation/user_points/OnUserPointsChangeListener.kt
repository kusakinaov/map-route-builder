package ku.olga.route_builder.presentation.user_points

import ku.olga.route_builder.domain.model.UserPoint

interface OnUserPointsChangeListener {
    fun onUserPointsChanged(userPoints: List<UserPoint>)
}