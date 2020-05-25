package ku.olga.route_builder.domain.model

data class BoundingBox(
        val latNorth: Double,
        val lonEast: Double,
        val latSouth: Double,
        val lonWest: Double
)