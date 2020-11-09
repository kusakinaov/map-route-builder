package ku.olga.user_points

import ku.olga.core_api.dto.UserPoint
import ku.olga.ui_core.base.BaseView
import ku.olga.user_points_map.UserPointsMapFragment

interface UserPointsView : BaseView, UserPointsMapFragment.BottomSheetCallback {
    fun bindUserPoints(userPoints: List<UserPoint>)
}