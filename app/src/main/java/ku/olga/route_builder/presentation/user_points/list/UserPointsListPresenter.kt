package ku.olga.route_builder.presentation.user_points.list

import kotlinx.coroutines.*
import ku.olga.route_builder.domain.model.UserPoint
import ku.olga.route_builder.domain.repository.PointsCacheRepository
import ku.olga.route_builder.presentation.base.BasePresenter

class UserPointsListPresenter(private val pointsRepository: PointsCacheRepository) :
    BasePresenter<UserPointsListView>() {
    private val userPoints = mutableListOf<UserPoint>()

    override fun attachView(view: UserPointsListView) {
        super.attachView(view)
        view.setUserPoints(userPoints)
        getUserPoints()
    }

    private fun getUserPoints() = CoroutineScope(Dispatchers.IO).launch {
        userPoints.clear()
        userPoints.addAll(pointsRepository.getUserPoints())
        withContext(Dispatchers.Main) {
            view?.apply {
                setUserPoints(userPoints)
                if (userPoints.isEmpty()) {
                    showEmpty()
                } else {
                    showUserPoints()
                }
            }
        }
    }
}