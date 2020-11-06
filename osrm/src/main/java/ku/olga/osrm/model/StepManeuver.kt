package ku.olga.osrm.model

data class StepManeuver(
    val location: Coordinates,
    val bearing_before: Int,
    val bearing_after: Int,
    val type: StepManeuverType,
    val modifier: StepManeuverModifier,
    val exit: Int?
)