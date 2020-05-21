package ku.olga.route_builder.presentation.user_points.root

import ku.olga.route_builder.domain.model.UserPoint
import ku.olga.route_builder.presentation.base.BaseView
import ku.olga.route_builder.presentation.user_points.map.UserPointsMapFragment

interface UserPointsView : BaseView, UserPointsMapFragment.BottomSheetCallback {
    fun isPressBackConsumed(): Boolean
    fun bindUserPoints(userPoints: List<UserPoint>)
}