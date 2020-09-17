package ku.olga.osrm.model

data class RouteStep(
    val distance: Float,
    val duration: Float,
    val geometry: String,
    val weight: Float,
    val name: String,
    val ref: String,
    val pronunciation: String,
    val destinations: String, //undefined
    val exits: String, //undefined
    val mode: String,
    val maneuver: StepManeuver,
    val intersections: List<Intersection>,
    val rotary_name: String,
    val rotary_pronunciation: String,
    val driving_side: DrivingSide
)