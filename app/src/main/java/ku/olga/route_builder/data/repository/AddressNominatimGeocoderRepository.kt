package ku.olga.route_builder.data.repository

import ku.olga.route_builder.domain.repository.AddressRepository
import org.osmdroid.bonuspack.location.GeocoderNominatim

class AddressNominatimGeocoderRepository(val geocoderNominatim: GeocoderNominatim) :
    AddressRepository {
    override suspend fun searchAddress(query: String?) =
        geocoderNominatim.getFromLocationName(query, MAX_ADDRESS_SEARCH_RESULTS)
            .map { it.toSearchAddress() }

    override suspend fun searchAddress(lat: Double, lon: Double) =
        geocoderNominatim.getFromLocation(lat, lon, MAX_ADDRESS_SEARCH_RESULTS)
            .map { it.toSearchAddress() }
}