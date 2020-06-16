package ku.olga.route_builder.data.repository.osrm

data class Waypoint(
    val waypoint_index: Int,
    val trips_index: Int,
    val hint: String,
    val location: List<Double>,
    val name: String
)