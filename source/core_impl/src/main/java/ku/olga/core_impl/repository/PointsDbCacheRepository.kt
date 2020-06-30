package ku.olga.core_impl.repository

import ku.olga.core_api.database.AppDatabaseContract
import ku.olga.core_api.dto.UserPoint
import ku.olga.core_api.repository.PointsCacheRepository
import javax.inject.Inject
import ku.olga.core_api.database.entity.UserPoint as RoomUserPoint

class PointsDbCacheRepository @Inject constructor(private val appDatabase: AppDatabaseContract) :
    PointsCacheRepository {
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

    override suspend fun saveOrder(points: List<UserPoint>) {
        appDatabase.userPointDao().updateOrders(points.map { it.toRoomUserPoint() })
    }

    private fun UserPoint.toRoomUserPoint() =
        RoomUserPoint(id, title, postalAddress, lat, lon, description, type, order)

    private fun RoomUserPoint.toUserPoint() =
        UserPoint(id, title, postalAddress, lat, lon, description, type, order)
}