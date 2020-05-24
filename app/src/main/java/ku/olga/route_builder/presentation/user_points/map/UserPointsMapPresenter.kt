package ku.olga.route_builder.presentation.user_points.map

import ku.olga.route_builder.domain.model.Coordinates
import ku.olga.route_builder.domain.model.UserPoint
import ku.olga.route_builder.presentation.App
import ku.olga.route_builder.presentation.base.BasePresenter

class UserPointsMapPresenter : BasePresenter<UserPointsMapView>() {
    private val userPoints = mutableListOf<UserPoint>()
    private var center: Coordinates? = null

    override fun attachView(view: UserPointsMapView) {
        super.attachView(view)
        if (userPoints.isEmpty()) {
            if (center != null) {
                moveToCenter()
            } else {
                moveToMyCoordinates()
            }
        } else bindUserPoints()
    }

    private fun moveToCenter() {
        center?.let {
            view?.moveTo(it.latitude, it.longitude, false)
        }
    }

    private fun moveToMyCoordinates() {
        App.application.getLastCoordinates().let {
            view?.moveTo(it.latitude, it.longitude, false)
        }
    }

    fun setUserPoints(userPoints: List<UserPoint>) {
        this.userPoints.clear()
        this.userPoints.addAll(userPoints)

        bindUserPoints()
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

    fun onCenterChanged(latitude: Double, longitude: Double) {
        center = Coordinates(latitude, longitude)
    }
}