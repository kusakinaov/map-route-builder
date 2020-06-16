package ku.olga.route_builder.data.repository.osrm

data class Trip(
    val legs: List<*>,
    val weight_name: String,
    val geometry: String,
    val weight: Int,
    val distance: Double,
    val duration: Int
) 