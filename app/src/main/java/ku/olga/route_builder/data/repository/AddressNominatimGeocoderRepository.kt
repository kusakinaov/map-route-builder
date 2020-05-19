package ku.olga.route_builder.data.repository

import android.location.Address
import ku.olga.route_builder.domain.model.SearchAddress
import ku.olga.route_builder.domain.repository.AddressRepository
import org.osmdroid.bonuspack.location.GeocoderNominatim
import java.lang.StringBuilder

class AddressNominatimGeocoderRepository(private val geocoderNominatim: GeocoderNominatim) :
    AddressRepository {
    override suspend fun searchAddress(query: String?) =
        geocoderNominatim.getFromLocationName(query, MAX_ADDRESS_SEARCH_RESULTS)
            .map { it.toSearchAddress() }

    override suspend fun searchAddress(lat: Double, lon: Double) =
        geocoderNominatim.getFromLocation(lat, lon, MAX_ADDRESS_SEARCH_RESULTS)
            .map { it.toSearchAddress() }

    private fun Address.toSearchAddress() = SearchAddress(buildPostalAddress(), latitude, longitude)

    private fun Address.buildPostalAddress(): String {
        val builder = StringBuilder()
        if (extras?.containsKey(NOMINATIM_EXTRAS_DISPLAY_NAME) == true) {
            builder.append(extras.getString(NOMINATIM_EXTRAS_DISPLAY_NAME))
        } else {
            for (i in 0..maxAddressLineIndex) {
                if (builder.isNotEmpty()) builder.append(", ")
                builder.append(getAddressLine(i))
            }
        }
        return builder.toString()
    }

    companion object {
        const val NOMINATIM_EXTRAS_DISPLAY_NAME = "display_name"
    }
}