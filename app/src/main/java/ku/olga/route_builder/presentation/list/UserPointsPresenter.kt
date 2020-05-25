package ku.olga.route_builder.presentation.list

import kotlinx.coroutines.*
import ku.olga.route_builder.domain.model.UserPoint
import ku.olga.route_builder.domain.repository.PointsCacheRepository
import ku.olga.route_builder.presentation.base.BasePresenter
import javax.inject.Inject

class UserPointsPresenter @Inject constructor(private val pointsRepository: PointsCacheRepository) :
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
}