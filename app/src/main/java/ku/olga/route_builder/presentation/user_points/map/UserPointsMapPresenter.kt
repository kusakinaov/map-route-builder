package ku.olga.route_builder.presentation.user_points.map

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ku.olga.route_builder.domain.model.UserPoint
import ku.olga.route_builder.domain.repository.PointsCacheRepository
import ku.olga.route_builder.presentation.base.BasePresenter

class UserPointsMapPresenter(private val pointsRepository: PointsCacheRepository) : BasePresenter<UserPointsMapView>() {
    private val userPoints = mutableListOf<UserPoint>()

    override fun attachView(view: UserPointsMapView) {
        super.attachView(view)
        bindUserPoints()
        getUserPoints()
    }

    private fun getUserPoints() = CoroutineScope(Dispatchers.IO).launch {
        userPoints.clear()
        userPoints.addAll(pointsRepository.getUserPoints())
        withContext(Dispatchers.Main) { bindUserPoints() }
    }

    private fun bindUserPoints() {
        view?.apply {
            setUserPoints(userPoints)
            when {
                userPoints.size == 1 -> moveTo(userPoints[0], false)
                userPoints.isNotEmpty() -> moveTo(userPoints, false)
            }
        }
    }

    fun onClickEditUserPoint(userPoint: UserPoint) {
        view?.apply {
            hideUserPoint()
            editUserPoint(userPoint)
        }
    }

    fun onClickMarker(userPoint: UserPoint): Boolean {
        view?.apply {
            moveTo(userPoint, true)
            showUserPoint(userPoint)
        }
        return true
    }
}