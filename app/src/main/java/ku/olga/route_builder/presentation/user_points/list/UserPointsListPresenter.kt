package ku.olga.route_builder.presentation.user_points.list

import ku.olga.route_builder.domain.model.UserPoint
import ku.olga.route_builder.presentation.base.BasePresenter

class UserPointsListPresenter() :
        BasePresenter<UserPointsListView>() {
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