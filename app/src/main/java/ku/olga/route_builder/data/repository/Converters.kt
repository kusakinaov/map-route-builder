package ku.olga.route_builder.data.repository

import android.location.Address
import ku.olga.route_builder.domain.model.SearchAddress
import java.lang.StringBuilder

fun Address.toSearchAddress() = SearchAddress(buildPostalAddress(), latitude, longitude)

fun Address.buildPostalAddress(): String {
    val builder = StringBuilder()
    for (i in 0..maxAddressLineIndex) {
        if (builder.isNotEmpty()) builder.append(", ")
        builder.append(getAddressLine(i))
    }
    return builder.toString()
}