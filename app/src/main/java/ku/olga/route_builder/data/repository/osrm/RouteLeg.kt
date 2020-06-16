package ku.olga.route_builder.data.repository.osrm

data class RouteLeg(
    val distance: Float,
    val duration: Float,
    val weight: Float,
    val summary: String,
    val steps: List<RouteStep>,
    val annotation: Annotation
)