package ku.olga.nominatim

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import ku.olga.nominatim.gson.BoundingBoxTypeAdapter
import ku.olga.nominatim.model.BoundingBox
import ku.olga.nominatim.model.Place
import java.net.URL

object AddressLookupService {
    private val gson = GsonBuilder()
        .registerTypeAdapter(BoundingBox::class.java, BoundingBoxTypeAdapter())
        .create()
    private const val URL_SEARCH = "https://nominatim.openstreetmap.org/lookup"
    private const val TAG: String = "nominatim_address_lookup_service"

    fun addressLookupSearch(
        places: List<Place>,
        language: String,
        debug: Boolean
    ): List<Place> {
        val url = URL_SEARCH + buildQuery(places, language, debug)
        println("${TAG}: ---> GET $url")
        val result = URL(url).readText()
        println("${TAG}: <-- GET $url\n$result")

        return mutableListOf<Place>().apply {
            addAll(gson.fromJson(result, object : TypeToken<List<Place>>() {}.type))
        }
    }

    private fun buildQuery(
        places: List<Place>,
        language: String,
        debug: Boolean
    ): String = StringBuilder().apply {
        append("?").append("$ACCEPT_LANGUAGE=$language")
        append("&").append("$DEBUG=${if (debug) 1 else 0}")
        append("&").append("$ADDRESS_DETAILS=1")
        append("&").append("$EXTRA_TAGS=1")
        append("&").append("$NAME_DETAILS=1")
        append("&").append("$FORMAT=$FORMAT_JSON")
        if (places.isNotEmpty()) {
            append("&").append("${OSM_IDS}=")
            for ((i, place) in places.withIndex()) {
                if (i > 0) append(",")
                append("${place.osm_type.char}${place.osm_id}")
            }
        }
    }.toString()
}