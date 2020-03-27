package ku.olga.route_builder.data.repository

import android.location.Address
import android.location.Geocoder
import ku.olga.route_builder.domain.model.SearchAddress
import ku.olga.route_builder.domain.repository.PointsRepository
import java.lang.StringBuilder

class PointsRepositoryImpl(private val geocoder: Geocoder) : PointsRepository {

    override suspend fun searchAddress(query: String?) =
        geocoder.getFromLocationName(query, 25).map { it.toSearchAddress() }.toList()

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