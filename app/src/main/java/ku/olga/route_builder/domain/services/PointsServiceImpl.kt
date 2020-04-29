package ku.olga.route_builder.domain.services

import ku.olga.route_builder.domain.model.UserPoint
import ku.olga.route_builder.domain.repository.AddressRepository
import ku.olga.route_builder.domain.repository.PointsCacheRepository

class PointsServiceImpl(
    private val addressRepository: AddressRepository,
    private val pointsCacheRepository: PointsCacheRepository
) : PointsService {
    override suspend fun searchAddress(query: String?) = addressRepository.searchAddress(query)

    override suspend fun searchAddress(lat: Double, lon: Double) =
        addressRepository.searchAddress(lat, lon)

    override suspend fun saveUserPoint(userPoint: UserPoint) =
        pointsCacheRepository.saveUserPoint(userPoint)

    override suspend fun getUserPoints() = pointsCacheRepository.getUserPoints()

    override suspend fun deleteUserPoint(userPoint: UserPoint) =
        pointsCacheRepository.deleteUserPoint(userPoint)
}