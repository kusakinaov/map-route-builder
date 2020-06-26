package ku.olga.core_api.repository

import ku.olga.core_api.dto.Coordinates

interface DirectionsRepository {
    suspend fun getDirections(points: List<Coordinates>): List<Coordinates>
}