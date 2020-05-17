package ku.olga.route_builder.data.repository

import ku.olga.route_builder.domain.model.Category
import ku.olga.route_builder.domain.repository.CategoryRepository

class CategoryNominatimRepository(val categories: Array<String>) : CategoryRepository {
    override suspend fun getCategories(query: String?): List<Category> =
        if (query.isNullOrEmpty()) {
            categories.map { Category(it) }.toList()
        } else {
            categories.filter { it.contains(it) }.map { Category(it) }.toList()
        }
}