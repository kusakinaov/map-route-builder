package ku.olga.route_builder.data.repository.osrm

data class Intersection(
    val location: List<Double>,
    val bearings: List<Int>,
    val classes: List<String>,
    val entry: List<Boolean>,
    val `in`: Int,
    val out: Int,
    val lanes: List<Lane>
)