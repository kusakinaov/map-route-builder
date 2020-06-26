package ku.olga.route_builder.data.repository.osrm

data class Annotation(
    val distance: List<Int>,
    val duration: List<Int>,
    val datasources: List<Int>,
    val nodes: List<Long>,
    val weight: List<Int>,
    val speed: Any,
    val metadata: AnnotationMetadata
)