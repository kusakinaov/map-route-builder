package ku.olga.osrm.model

data class Waypoint(
    val name: String,
    val location: Coordinates,
    val distance: Int,
    val hint: String,
    val waypoint_index: Int,
    val trips_index: Int
)