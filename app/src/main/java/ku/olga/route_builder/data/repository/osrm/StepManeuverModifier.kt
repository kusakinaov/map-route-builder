package ku.olga.route_builder.data.repository.osrm

enum class StepManeuverModifier(val text: String) {
    uturn("uturn"),
    sharp_right("sharp right"),
    right("right"),
    slight_right("slight right"),
    straight("straight"),
    slight_left("slight left"),
    left("left"),
    sharp_left("sharp left")
}