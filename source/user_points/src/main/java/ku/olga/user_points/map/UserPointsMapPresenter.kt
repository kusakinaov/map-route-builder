package ku.olga.user_points.map

import android.content.SharedPreferences
import ku.olga.core_api.dto.Coordinates
import ku.olga.core_api.dto.UserPoint
import ku.olga.ui_core.base.BasePresenter
import ku.olga.ui_core.utils.getLastCoordinates

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ku.olga.core_api.repository.DirectionsRepository
import org.osmdroid.util.GeoPoint
import javax.inject.Inject

class UserPointsMapPresenter @Inject constructor(
    private val directionsRepository: DirectionsRepository,
    private val preferences: SharedPreferences
) :
    BasePresenter<UserPointsMapView>() {
    private val userPoints = mutableListOf<UserPoint>()
    private var center: Coordinates? = null
    private var zoomLevel: Double =
        DEFAULT_ZOOM_LEVEL

    override fun attachView(view: UserPointsMapView) {
        super.attachView(view)
        when {
            center != null -> moveToCenter()
            userPoints.isEmpty() -> moveToMyCoordinates()
            userPoints.isNotEmpty() -> bindUserPoints()
        }
    }

    private fun moveToCenter() {
        center?.let {
            view?.moveTo(it.latitude, it.longitude, zoomLevel, false)
        }
    }

    private fun moveToMyCoordinates() {
        getLastCoordinates(preferences).let {
            view?.moveTo(it.latitude, it.longitude, zoomLevel, false)
        }
    }

    fun setUserPoints(userPoints: List<UserPoint>) {
        this.userPoints.clear()
        this.userPoints.addAll(userPoints)

        bindUserPoints()
        if (userPoints.size >= 2) buildDirections(userPoints)
    }

    private fun buildDirections(userPoints: List<UserPoint>) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val coordinates = directionsRepository.getDirections(userPoints.map {
                    Coordinates(
                        it.lat,
                        it.lon
                    )
                })
                withContext(Dispatchers.Main) { view?.showDirections(coordinates) }
            } catch (exc: Exception) {
                exc.printStackTrace()
                withContext(Dispatchers.Main) { view?.showDirectionsError() }
            }
        }

    private fun bindUserPoints() {
        view?.apply {
            setUserPoints(userPoints)
            when {
                userPoints.size == 1 -> userPoints[0].let {
                    moveTo(
                        it.lat,
                        it.lon,
                        zoomLevel,
                        false
                    )
                }
                userPoints.isNotEmpty() -> moveTo(userPoints.map { GeoPoint(it.lat, it.lon) }, false)
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
            moveTo(userPoint.lat, userPoint.lon, zoomLevel, true)
            showUserPoint(userPoint)
        }
        return true
    }

    fun onCenterChanged(latitude: Double, longitude: Double, zoomLevel: Double) {
        center = Coordinates(latitude, longitude)
        this.zoomLevel = zoomLevel
    }

    companion object {
        private const val DEFAULT_ZOOM_LEVEL = 15.0
    }
}