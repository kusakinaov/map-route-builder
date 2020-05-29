package ku.olga.user_points.list

import ku.olga.core_api.dto.UserPoint
import ku.olga.ui_core.base.BasePresenter
import javax.inject.Inject

class UserPointsListPresenter @Inject constructor(): BasePresenter<UserPointsListView>() {
    private val userPoints = mutableListOf<UserPoint>()

    override fun attachView(view: UserPointsListView) {
        super.attachView(view)
        view.setUserPoints(userPoints)
    }

    fun setUserPoints(userPoints: List<UserPoint>) {
        this.userPoints.clear()
        this.userPoints.addAll(userPoints)

        view?.setUserPoints(userPoints)
    }
}