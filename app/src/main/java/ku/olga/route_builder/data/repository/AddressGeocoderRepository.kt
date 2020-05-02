package ku.olga.route_builder.data.repository

import android.location.Geocoder
import ku.olga.route_builder.domain.repository.AddressRepository

class AddressGeocoderRepository(private val geocoder: Geocoder) : AddressRepository {
    override suspend fun searchAddress(query: String?) =
        geocoder.getFromLocationName(query, MAX_ADDRESS_SEARCH_RESULTS).map { it.toSearchAddress() }.toList()

    override suspend fun searchAddress(lat: Double, lon: Double) =
        geocoder.getFromLocation(lat, lon, MAX_ADDRESS_SEARCH_RESULTS).map { it.toSearchAddress() }.toList()


}