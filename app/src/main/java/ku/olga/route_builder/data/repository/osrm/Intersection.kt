package ku.olga.route_builder.data.repository.osrm

import ku.olga.route_builder.domain.model.Coordinates

data class Intersection(
    val location: Coordinates,
    val bearings: List<Int>,
    val classes: List<String>,
    val entry: List<Boolean>,
    val `in`: Int,
    val out: Int,
    val lanes: List<Lane>
)