package ku.olga.core_api.repository

import ku.olga.core_api.dto.UserPoint

interface PointsCacheRepository {
    suspend fun saveUserPoint(userPoint: UserPoint): Long
    suspend fun getUserPoints(): List<UserPoint>
    suspend fun deleteUserPoint(userPoint: UserPoint): Int
}