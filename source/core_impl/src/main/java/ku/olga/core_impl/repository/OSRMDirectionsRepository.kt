package ku.olga.core_impl.repository

import ku.olga.core_api.dto.Coordinates
import ku.olga.core_api.repository.DirectionsRepository
import ku.olga.osrm.OSRMHelper
import javax.inject.Inject

import ku.olga.osrm.model.Coordinates as OSRMCoordinates

class OSRMDirectionsRepository @Inject constructor() : DirectionsRepository {
    override suspend fun getDirections(points: List<Coordinates>): List<Coordinates> =
        OSRMHelper.getFootDirections(points.map { it.toOSRMCoordinates() })
            .map { it.toDTOCoordinates() }

    fun Coordinates.toOSRMCoordinates() = OSRMCoordinates(latitude, longitude)
    fun OSRMCoordinates.toDTOCoordinates() = Coordinates(latitude, longitude)
}