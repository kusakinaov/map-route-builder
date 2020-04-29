package ku.olga.route_builder.domain.repository

import ku.olga.route_builder.domain.model.UserPoint

interface PointsCacheRepository {
    suspend fun saveUserPoint(userPoint: UserPoint): Long
    suspend fun getUserPoints(): List<UserPoint>
    suspend fun deleteUserPoint(userPoint: UserPoint): Int
}