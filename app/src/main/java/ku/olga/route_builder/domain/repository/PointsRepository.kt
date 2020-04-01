package ku.olga.route_builder.domain.repository

import ku.olga.route_builder.domain.model.SearchAddress

interface PointsRepository {
    suspend fun searchAddress(query: String?): List<SearchAddress>
    suspend fun searchAddress(lat: Double, lon: Double): List<SearchAddress>
}