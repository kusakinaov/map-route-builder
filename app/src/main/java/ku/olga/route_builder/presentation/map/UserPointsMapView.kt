package ku.olga.route_builder.presentation.map

import android.os.Bundle
import ku.olga.route_builder.domain.model.UserPoint
import ku.olga.route_builder.presentation.base.BaseView

interface UserPointsMapView : BaseView {
    fun setUserPoints(userPoints: List<UserPoint>)
    fun animateTo(userPoints: List<UserPoint>)
    fun onAttach(bundle: Bundle?)
    fun onStart()
    fun onResume()
    fun onPause()
    fun onStop()
    fun hideBottomSheet(): Boolean
    fun editUserPoint(userPoint: UserPoint)
    fun showBottomMenu(userPoint: UserPoint)
}