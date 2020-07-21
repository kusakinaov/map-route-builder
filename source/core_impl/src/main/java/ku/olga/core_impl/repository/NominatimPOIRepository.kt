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
import ku.olga.core_impl.repository.model.Operator
import ku.olga.core_impl.repository.model.Plural
import ku.olga.core_impl.repository.model.Tag
import ku.olga.nominatim.NominatimHelper
import ku.olga.nominatim.model.Coordinates
import ku.olga.nominatim.model.Place
import java.io.InputStreamReader
import java.util.*
import kotlin.Comparator
import org.osmdroid.util.BoundingBox as ApiBoundingBox
import ku.olga.nominatim.model.BoundingBox as NominatimBoundingBox

class NominatimPOIRepository(
        private val assetManager: AssetManager,
        private val gson: Gson,
        private val poiProvider: NominatimPOIProvider
) : POIRepository {
    private val categories = mutableListOf<Category>()

    private fun initCategories() {
        categories.clear()
        val reader = InputStreamReader(assetManager.open("ru_nominatim_poi_tags.json"))
        try {
            val list: List<Tag> = gson.fromJson(reader, object : TypeToken<List<Tag>>() {}.type)
            categories.addAll(
                    list
                            .filter { it.key == "amenity" }
                            .filter { it.operator == Operator.`-` }
                            .filter { it.plural == Plural.N }
                            .map { Category(it.key, it.value, it.word) }
            )
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

    override suspend fun getPOIs(query: String?, boundingBox: BoundingBox, category: Category): List<AppPOI> =
            NominatimHelper.searchPOI(query = query,
                    boundingBox = boundingBox.toNominatimBoundingBox(),
                    amenityTag = category.value).map { it.toPOI() }.toList()

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

    private fun Place.toPOI() = AppPOI(
            place_id,
            lat,
            lon,
            display_name,
            licence,
            icon,
            "",
            1,
            type,
            type
    )

    companion object {
        fun BoundingBox.toApiBoundingBox() =
                ApiBoundingBox(latNorth, lonEast, latSouth, lonWest)

        fun BoundingBox.toNominatimBoundingBox() =
                NominatimBoundingBox(Coordinates(latSouth, lonWest), Coordinates(latNorth, lonEast))
    }
}