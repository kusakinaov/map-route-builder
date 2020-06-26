package ku.olga.route_builder.data.repository.osrm

data class Route(
    val distance: Float,
    val duration: Float,
    val geometry: String,
    val weight_name: String,
    val weight: Float,
    val legs: List<RouteLeg>
)