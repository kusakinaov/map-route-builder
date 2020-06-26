package ku.olga.route_builder.data.repository.osrm

import ku.olga.core_api.dto.Coordinates

data class Waypoint(
    val name: String,
    val location: Coordinates,
    val distance: Int,
    val hint: String,
    val waypoint_index: Int,
    val trips_index: Int
)