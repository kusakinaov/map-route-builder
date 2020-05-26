package ku.olga.core_impl.repository

import android.content.res.AssetManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ku.olga.core_api.dto.Category
import ku.olga.core_api.repository.POIRepository
import org.osmdroid.bonuspack.location.NominatimPOIProvider
import org.osmdroid.bonuspack.location.POI
import ku.olga.core_api.dto.POI as AppPOI
import ku.olga.core_api.dto.BoundingBox
import java.io.InputStreamReader
import java.util.*
import kotlin.Comparator
import org.osmdroid.util.BoundingBox as ApiBoundingBox

class NominatimPOIRepository(
    private val assetManager: AssetManager,
    private val gson: Gson,
    private val poiProvider: NominatimPOIProvider
) : POIRepository {
    private val categories = mutableListOf<Category>()

    private fun initCategories() {
        categories.clear()
        val reader = InputStreamReader(assetManager.open("nominatin_poi_tags.json"))
        try {
            val map: Map<String, Map<String, String>> = gson.fromJson(reader,
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

    override suspend fun getPOIs(boundingBox: BoundingBox, category: Category): List<AppPOI> =
        poiProvider.getPOIInside(boundingBox.toApiBoundingBox(), category.key, 100)
            ?.map { it.toAppPOI() }?.toList() ?: emptyList()

    private fun POI.toAppPOI() = AppPOI(
        mId,
        mLocation.latitude,
        mLocation.longitude,
        "",
        mDescription,
        mThumbnailPath,
        mUrl,
        mRank,
        mCategory,
        mType
    )

    companion object {
        private const val AMENITY = "amenity"

        fun BoundingBox.toApiBoundingBox() =
            ApiBoundingBox(latNorth, lonEast, latSouth, lonWest)
    }
}