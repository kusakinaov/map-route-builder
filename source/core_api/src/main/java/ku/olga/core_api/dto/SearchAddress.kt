package ku.olga.core_api.dto

import java.io.Serializable

data class SearchAddress(
    val postalAddress: String,
    val lat: Double,
    val lon: Double
) : Serializable