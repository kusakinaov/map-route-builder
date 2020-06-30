package ku.olga.nominatim

interface SearchAddressService {
    suspend fun searchByQuery(query: String)
}