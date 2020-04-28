package ku.olga.route_builder.data.repository

import android.location.Geocoder
import ku.olga.route_builder.data.room.AppDatabase
import ku.olga.route_builder.domain.model.UserPoint
import ku.olga.route_builder.domain.repository.PointsRepository
import ku.olga.route_builder.data.toRoomUserPoint
import ku.olga.route_builder.data.toSearchAddress
import ku.olga.route_builder.data.toUserPoint

class PointsRepositoryImpl(private val appDatabase: AppDatabase, private val geocoder: Geocoder) :
    PointsRepository {

    override suspend fun searchAddress(query: String?) =
        geocoder.getFromLocationName(query, 25).map { it.toSearchAddress() }.toList()

    override suspend fun searchAddress(lat: Double, lon: Double) =
        geocoder.getFromLocation(lat, lon, 25).map { it.toSearchAddress() }.toList()

    override suspend fun saveUserPoint(userPoint: UserPoint): Long {
        if (userPoint.id == 0L) {
            userPoint.id = appDatabase.userPointDao().getMaxIdx() + 1
        }
        return appDatabase.userPointDao().insertOrUpdate(userPoint.toRoomUserPoint())
    }

    override suspend fun getUserPoints() =
        appDatabase.userPointDao().getAll().map { it.toUserPoint() }.toList()

    override suspend fun deleteUserPoint(userPoint: UserPoint) =
        appDatabase.userPointDao().delete(userPoint.toRoomUserPoint())
}