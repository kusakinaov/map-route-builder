package ku.olga.core_api.repository

import ku.olga.core_api.dto.BoundingBox
import ku.olga.core_api.dto.Category
import ku.olga.core_api.dto.POI

interface POIRepository {
    suspend fun getCategories(query: String?): List<Category>
    suspend fun getPOIs(query: String?, boundingBox: BoundingBox, category: Category): List<POI>
}