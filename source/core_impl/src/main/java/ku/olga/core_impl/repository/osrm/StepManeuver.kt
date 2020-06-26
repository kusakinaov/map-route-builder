package ku.olga.route_builder.data.repository.osrm

import ku.olga.core_api.dto.Coordinates

data class StepManeuver(
    val location: Coordinates,
    val bearing_before: Int,
    val bearing_after: Int,
    val type: StepManeuverType,
    val modifier: StepManeuverModifier,
    val exit: Int?
)