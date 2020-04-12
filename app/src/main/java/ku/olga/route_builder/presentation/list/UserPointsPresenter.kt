package ku.olga.route_builder.presentation.list

import kotlinx.coroutines.*
import ku.olga.route_builder.domain.model.UserPoint
import ku.olga.route_builder.domain.repository.PointsRepository
import ku.olga.route_builder.presentation.base.BasePresenter

class UserPointsPresenter(private val pointsRepository: PointsRepository) :
    BasePresenter<UserPointsView>() {
    private val userPoints = mutableListOf<UserPoint>()

    override fun attachView(view: UserPointsView) {
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

    override fun detachView() {
        super.detachView()
    }
}