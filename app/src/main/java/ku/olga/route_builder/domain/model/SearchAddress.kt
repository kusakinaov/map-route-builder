package ku.olga.route_builder.domain.model

import java.io.Serializable

data class SearchAddress(
    val postalAddress: String,
    val lat: Double,
    val lon: Double
) : Serializable