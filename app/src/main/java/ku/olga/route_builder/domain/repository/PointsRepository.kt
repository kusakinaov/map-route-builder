package ku.olga.route_builder.domain.repository

import ku.olga.route_builder.domain.model.SearchAddress
import ku.olga.route_builder.domain.model.UserPoint

interface PointsRepository {
    suspend fun searchAddress(query: String?): List<SearchAddress>
    suspend fun searchAddress(lat: Double, lon: Double): List<SearchAddress>
    suspend fun saveUserPoint(userPoint: UserPoint): Long
    suspend fun getUserPoints(): List<UserPoint>
    suspend fun deleteUserPoint(userPoint: UserPoint): Int
}