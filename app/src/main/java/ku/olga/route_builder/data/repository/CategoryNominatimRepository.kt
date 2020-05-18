package ku.olga.route_builder.data.repository

import ku.olga.route_builder.domain.model.Category
import ku.olga.route_builder.domain.repository.CategoryRepository
import java.util.*

class CategoryNominatimRepository(tags: Array<String>) : CategoryRepository {
    private val categories = mutableListOf<Category>()

    init {
        for (tag in tags) categories.add(Category(tag, tag))
    }

    override suspend fun getCategories(query: String?): List<Category> =
        if (query.isNullOrEmpty()) {
            categories.toList()
        } else {
            val lowerQuery = query.toLowerCase(Locale.ROOT)
            categories.filter { it.title.toLowerCase(Locale.ROOT).contains(lowerQuery) }.toList()
        }
}