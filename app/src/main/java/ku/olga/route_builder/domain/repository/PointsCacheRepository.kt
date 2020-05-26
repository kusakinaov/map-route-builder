package ku.olga.route_builder.domain.repository

import ku.olga.core_api.dto.UserPoint

interface PointsCacheRepository {
    suspend fun saveUserPoint(userPoint: UserPoint): Long
    suspend fun getUserPoints(): List<UserPoint>
    suspend fun deleteUserPoint(userPoint: UserPoint): Int
}