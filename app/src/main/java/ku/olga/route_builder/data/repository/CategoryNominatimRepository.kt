package ku.olga.route_builder.data.repository

import android.content.res.AssetManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ku.olga.core_api.dto.Category
import ku.olga.route_builder.domain.repository.CategoryRepository
import java.io.InputStreamReader
import java.util.Locale
import javax.inject.Inject

class CategoryNominatimRepository @Inject constructor(private val assetManager: AssetManager, private val gson: Gson) :
    CategoryRepository {
    private val categories = mutableListOf<Category>()

    override suspend fun getCategories(query: String?): List<Category> {
        if (categories.isEmpty()) initCategories()

        val lowerQuery = query?.toLowerCase(Locale.ROOT) ?: ""
        val filtered = mutableListOf<Category>()

        if (query.isNullOrEmpty()) {
            filtered.addAll(categories)
        } else {
            filtered.addAll(categories.filter {
                it.title.toLowerCase(Locale.ROOT).contains(lowerQuery)
            }.toList())
        }
        return filtered
    }

    private fun initCategories() {
        categories.clear()
        val reader = InputStreamReader(assetManager.open("nominatin_poi_tags.json"))
        try {
            val map: Map<String, Map<String, String>> = gson.fromJson(
                reader,
                object : TypeToken<Map<String, Map<String, String>>>() {}.type
            )
            for ((k, v) in map[AMENITY] ?: emptyMap()) {
                categories.add(Category(k, v))
            }
        } finally {
            reader.close()
        }
        categories.sortWith(Comparator { left, right -> left.title.compareTo(right.title) })
    }

    companion object {
        private const val AMENITY = "amenity"
    }
}