package ku.olga.route_builder.presentation.user_points.root

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ku.olga.core_api.dto.UserPoint
import ku.olga.route_builder.domain.repository.PointsCacheRepository
import ku.olga.route_builder.presentation.base.BasePresenter
import javax.inject.Inject

class UserPointsPresenter @Inject constructor(private val pointsRepository: PointsCacheRepository) :
    BasePresenter<UserPointsView>() {
    private val userPoints = mutableListOf<UserPoint>()

    override fun attachView(view: UserPointsView) {
        super.attachView(view)
        bindUserPoints()
        getUserPoints()
    }

    private fun getUserPoints() = CoroutineScope(Dispatchers.IO).launch {
        userPoints.clear()
        userPoints.addAll(pointsRepository.getUserPoints())
        withContext(Dispatchers.Main) {
            bindUserPoints()
        }
    }

    fun bindUserPoints() {
        view?.bindUserPoints(userPoints)
    }
}