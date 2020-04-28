package ku.olga.route_builder.data.repository

import android.location.Address
import android.location.Geocoder
import ku.olga.route_builder.data.room.AppDatabase
import ku.olga.route_builder.domain.model.UserPoint
import ku.olga.route_builder.domain.repository.PointsRepository
import ku.olga.route_builder.domain.model.SearchAddress
import java.lang.StringBuilder
import ku.olga.route_builder.data.room.entity.UserPoint as RoomUserPoint

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

    private fun Address.toSearchAddress() = SearchAddress(buildPostalAddress(), latitude, longitude)

    private fun Address.buildPostalAddress(): String {
        val builder = StringBuilder()
        for (i in 0..maxAddressLineIndex) {
            if (builder.isNotEmpty()) builder.append(", ")
            builder.append(getAddressLine(i))
        }
        return builder.toString()
    }

    private fun UserPoint.toRoomUserPoint() = RoomUserPoint(id, title, postalAddress, lat, lon, description)

    private fun RoomUserPoint.toUserPoint() =
        UserPoint(id, title, postalAddress, lat, lon, description)
}