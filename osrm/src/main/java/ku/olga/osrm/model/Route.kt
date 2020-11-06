package ku.olga.osrm.model

data class Route(
    val distance: Float,
    val duration: Float,
    val geometry: String,
    val weight_name: String,
    val weight: Float,
    val legs: List<RouteLeg>
)