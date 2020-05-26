package ku.olga.route_builder.domain.repository

import ku.olga.core_api.dto.Category

interface CategoryRepository {
    suspend fun getCategories(query: String?): List<Category>
}