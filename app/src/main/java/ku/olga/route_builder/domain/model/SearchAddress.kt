package ku.olga.route_builder.domain.model

data class SearchAddress(
    val postalAddress: String,
    val lat: Double,
    val lon: Double
)