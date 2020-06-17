package ku.olga.route_builder.data.repository.osrm

import ku.olga.route_builder.domain.model.Coordinates

data class StepManeuver(
    val location: Coordinates,
    val bearing_before: Int,
    val bearing_after: Int,
    val type: StepManeuverType,
    val modifier: StepManeuverModifier,
    val exit: Int?
)