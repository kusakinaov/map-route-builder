package ku.olga.route_builder.data.repository.osrm

data class StepManeuver(
    val location: List<Double>,
    val bearing_before: Int,
    val bearing_after: Int,
    val type: StepManeuverType,
    val modifier: StepManeuverModifier,
    val exit: Int?
)