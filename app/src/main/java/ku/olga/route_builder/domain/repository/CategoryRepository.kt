package ku.olga.route_builder.domain.repository

import ku.olga.route_builder.domain.model.Category

interface CategoryRepository {
    suspend fun getCategories(query: String?): List<Category>
}