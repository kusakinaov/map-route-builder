package ku.olga.route_builder.domain.repository

import ku.olga.route_builder.domain.model.BoundingBox
import ku.olga.route_builder.domain.model.Category
import ku.olga.route_builder.domain.model.POI

interface POIRepository {
    suspend fun getPOIs(boundingBox: BoundingBox, category: Category): List<POI>
}