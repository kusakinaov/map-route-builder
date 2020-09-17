package ku.olga.osrm.model

data class Intersection(
    val location: Coordinates,
    val bearings: List<Int>,
    val classes: List<String>,
    val entry: List<Boolean>,
    val `in`: Int,
    val out: Int,
    val lanes: List<Lane>
)