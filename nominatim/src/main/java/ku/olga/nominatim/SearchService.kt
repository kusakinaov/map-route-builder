package ku.olga.nominatim

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import ku.olga.nominatim.gson.BoundingBoxTypeAdapter
import ku.olga.nominatim.model.BoundingBox
import ku.olga.nominatim.model.Place
import java.net.URL
import java.net.URLEncoder
import java.util.Locale

object SearchService {
    private val gson = GsonBuilder()
        .registerTypeAdapter(BoundingBox::class.java, BoundingBoxTypeAdapter())
        .create()
    private const val URL_SEARCH = "https://nominatim.openstreetmap.org/search"
    private const val TAG: String = "nominatim_search_service"

    fun search(
        query: String? = null,
        boundingBox: BoundingBox? = null,
        amenityTag: String? = null,
        language: String = Locale.getDefault().language,
        limit: Int = MAX_LIMIT,
        debug: Boolean = false
    ): List<Place> {
        val places = mutableListOf<Place>()

        val url = URL_SEARCH + buildQuery(
            query,
            boundingBox,
            boundingBox != null,
            amenityTag,
            language,
            limit,
            debug
        )
        println("$TAG: ---> GET $url")
        val result = URL(url).readText()
        println("$TAG: <-- GET $url\n$result")
        places.addAll(gson.fromJson(result, object : TypeToken<List<Place>>() {}.type))

        return places
    }

    private fun buildQuery(
        query: String? = null,
        boundingBox: BoundingBox? = null,
        bound: Boolean = false,
        amenityTag: String? = null,
        language: String,
        limit: Int,
        debug: Boolean
    ): String = StringBuilder().apply {
        append("?").append("$ACCEPT_LANGUAGE=$language")
        append("&").append("$DEBUG=${if (debug) 1 else 0}")

        if (query?.isNotEmpty() == true || amenityTag?.isNotEmpty() == true) {
            append("&").append("$QUERY=")
            if (query?.isNotEmpty() == true) {
                append(URLEncoder.encode(query))
                if (amenityTag?.isNotEmpty() == true) {
                    append("+")
                }
            }
            if (amenityTag?.isNotEmpty() == true) {
                append(URLEncoder.encode(amenityTag))
            }
        }
        append("&").append("$ADDRESS_DETAILS=1")
        append("&").append("$EXTRA_TAGS=1")
        append("&").append("$NAME_DETAILS=1")
        append("&").append("$LIMIT=$limit")
        append("&").append("$FORMAT=$FORMAT_JSON")
        boundingBox?.let {
            append("&").append("$VIEWBOX=${it.southWest.longitude},${it.northEast.latitude},${it.northEast.longitude},${it.southWest.latitude}")
            append("&").append("$BOUNDED=${if (bound) 1 else 0}")
        }
    }.toString()
}