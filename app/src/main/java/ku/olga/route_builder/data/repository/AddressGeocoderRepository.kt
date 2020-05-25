package ku.olga.route_builder.data.repository

import android.location.Address
import android.location.Geocoder
import ku.olga.route_builder.domain.model.SearchAddress
import ku.olga.route_builder.domain.repository.AddressRepository
import java.lang.StringBuilder
import javax.inject.Inject

class AddressGeocoderRepository @Inject constructor(private val geocoder: Geocoder) : AddressRepository {
    override suspend fun searchAddress(query: String?) =
        geocoder.getFromLocationName(query, MAX_ADDRESS_SEARCH_RESULTS).map { it.toSearchAddress() }
            .toList()

    override suspend fun searchAddress(lat: Double, lon: Double) =
        geocoder.getFromLocation(lat, lon, MAX_ADDRESS_SEARCH_RESULTS).map { it.toSearchAddress() }
            .toList()

    private fun Address.toSearchAddress() = SearchAddress(buildPostalAddress(), latitude, longitude)

    private fun Address.buildPostalAddress(): String {
        val builder = StringBuilder()
        for (i in 0..maxAddressLineIndex) {
            if (builder.isNotEmpty()) builder.append(", ")
            builder.append(getAddressLine(i))
        }
        return builder.toString()
    }
}