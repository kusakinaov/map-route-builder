package ku.olga.route_builder.data.repository

import ku.olga.route_builder.data.room.AppDatabase
import ku.olga.route_builder.domain.model.UserPoint
import ku.olga.route_builder.domain.repository.PointsCacheRepository
import ku.olga.route_builder.data.room.entity.UserPoint as RoomUserPoint

class PointsDbCacheRepository(private val appDatabase: AppDatabase) : PointsCacheRepository {
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

    private fun UserPoint.toRoomUserPoint() =
        RoomUserPoint(id, title, postalAddress, lat, lon, description, type)

    private fun RoomUserPoint.toUserPoint() =
        UserPoint(id, title, postalAddress, lat, lon, description, type)
}