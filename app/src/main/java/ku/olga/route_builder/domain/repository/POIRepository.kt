package ku.olga.route_builder.domain.repository

import ku.olga.route_builder.domain.model.BoundingBox
import ku.olga.core_api.dto.Category
import ku.olga.core_api.dto.POI

interface POIRepository {
    suspend fun getPOIs(boundingBox: BoundingBox, category: Category): List<POI>
}