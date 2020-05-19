package ku.olga.route_builder.data.repository

import android.content.res.AssetManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ku.olga.route_builder.domain.model.Category
import ku.olga.route_builder.domain.repository.CategoryRepository
import java.io.InputStreamReader
import java.util.*

class CategoryNominatimRepository(private val assetManager: AssetManager, private val gson: Gson) :
    CategoryRepository {
    private val categories = mutableMapOf<String, List<Category>>()

    override suspend fun getCategories(query: String?): List<Category> {
        if (categories.isEmpty()) initCategories()

        val lowerQuery = query?.toLowerCase(Locale.ROOT) ?: ""
        val filtered = mutableListOf<Category>()

        for (list in categories.values) {
            if (query.isNullOrEmpty()) {
                filtered.addAll(list)
            } else {
                filtered.addAll(list.filter {
                    it.title.toLowerCase(Locale.ROOT).contains(lowerQuery)
                }.toList())
            }
        }
        return filtered
    }

    private fun initCategories() {
        categories.clear()
        val reader = InputStreamReader(assetManager.open("nominatin_poi_tags.txt"))
        try {
            val map: Map<String, Map<String, String>> = gson.fromJson(
                reader,
                object : TypeToken<Map<String, Map<String, String>>>() {}.type
            )
            for ((key, value) in map) {
                val list = mutableListOf<Category>()
                for ((k, v) in value) {
                    list.add(Category(k, v))
                }
                categories[key] = list
            }
        } finally {
            reader.close()
        }
    }
}