package ku.olga.core_api.repository

import ku.olga.core_api.dto.SearchAddress

interface AddressRepository {
    suspend fun searchAddress(query: String?): List<SearchAddress>
    suspend fun searchAddress(lat: Double, lon: Double): List<SearchAddress>
}