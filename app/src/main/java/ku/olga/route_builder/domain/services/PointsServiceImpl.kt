package ku.olga.route_builder.domain.services

import ku.olga.route_builder.domain.model.UserPoint
import ku.olga.route_builder.domain.repository.PointsRepository

class PointsServiceImpl(private val repository: PointsRepository) : PointsService {
    override suspend fun searchAddress(query: String?) = repository.searchAddress(query)

    override suspend fun searchAddress(lat: Double, lon: Double) =
        repository.searchAddress(lat, lon)

    override suspend fun saveUserPoint(userPoint: UserPoint) = repository.saveUserPoint(userPoint)

    override suspend fun getUserPoints() = repository.getUserPoints()

    override suspend fun deleteUserPoint(userPoint: UserPoint) =
        repository.deleteUserPoint(userPoint)
}