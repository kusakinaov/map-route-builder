package ku.olga.route_builder.data.repository.osrm

data class Waypoint(
    val name: String,
    val location: List<Double>,
    val distance: Int,
    val hint: String,
    val waypoint_index: Int,
    val trips_index: Int
)