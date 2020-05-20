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
        view.setUserPoints(userPoints)
        getUserPoints()
    }

    private fun getUserPoints() = CoroutineScope(Dispatchers.IO).launch {
        userPoints.clear()
        userPoints.addAll(pointsRepository.getUserPoints())
        withContext(Dispatchers.Main) { bindUserPoints() }
    }

    fun bindUserPoints() {
        view?.apply {
            setUserPoints(userPoints)
            animateTo(userPoints)
        }
    }

    fun onClickEditUserPoint(userPoint: UserPoint) {
        view?.apply {
            hideBottomSheet()
            editUserPoint(userPoint)
        }
    }

    fun onClickMarker(position: Int) {
        view?.apply {
            val userPoint = userPoints[position]
            showBottomMenu(userPoint)
            animateTo(listOf(userPoint))
        }
    }
}