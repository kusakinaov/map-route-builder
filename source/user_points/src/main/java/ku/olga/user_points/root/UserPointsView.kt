package ku.olga.user_points.root

import ku.olga.core_api.dto.UserPoint
import ku.olga.ui_core.base.BaseView
import ku.olga.user_points.map.UserPointsMapFragment

interface UserPointsView : BaseView, UserPointsMapFragment.BottomSheetCallback {
    fun bindUserPoints(userPoints: List<UserPoint>)
}