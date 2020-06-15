package ku.olga.route_builder.domain.repository

import ku.olga.route_builder.domain.model.Coordinates

interface DirectionsRepository {
    suspend fun getDirections(points: List<Coordinates>): List<Coordinates>
}