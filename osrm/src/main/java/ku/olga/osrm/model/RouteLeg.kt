package ku.olga.osrm.model

data class RouteLeg(
    val distance: Float,
    val duration: Float,
    val weight: Float,
    val summary: String,
    val steps: List<RouteStep>,
    val annotation: Annotation
)