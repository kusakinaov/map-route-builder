package ku.olga.nominatim

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import ku.olga.nominatim.gson.BoundingBoxTypeAdapter
import ku.olga.nominatim.model.BoundingBox
import ku.olga.nominatim.model.Place
import java.net.URL

object ReverseSearchService {
    private val gson = GsonBuilder()
        .registerTypeAdapter(BoundingBox::class.java, BoundingBoxTypeAdapter())
        .create()
    private const val URL_SEARCH = "https://nominatim.openstreetmap.org/reverse"
    private const val TAG: String = "nominatim_reverse_service"

    fun reverseSearch(
        latitude: Double,
        longitude: Double,
        zoom: Int,
        language: String,
        debug: Boolean
    ): List<Place> {
        val places = mutableListOf<Place>()

        val url = URL_SEARCH + buildQuery(latitude, longitude, zoom, language, debug)
        println("${TAG}: ---> GET $url")
        val result = URL(url).readText()
        println("${TAG}: <-- GET $url\n$result")
        places.addAll(gson.fromJson(result, object : TypeToken<List<Place>>() {}.type))

        return places
    }

    private fun buildQuery(
        latitude: Double,
        longitude: Double,
        zoom: Int,
        language: String,
        debug: Boolean
    ): String = StringBuilder().apply {
        append("?").append("$ACCEPT_LANGUAGE=$language")
        append("&").append("$DEBUG=${if (debug) 1 else 0}")
        append("&").append("$LATITUDE=$latitude")
        append("&").append("$LONGITUDE=$longitude")
        append("&").append("$ADDRESS_DETAILS=1")
        append("&").append("$EXTRA_TAGS=1")
        append("&").append("$NAME_DETAILS=1")
        append("&").append("$ZOOM=$zoom")
        append("&").append("$FORMAT=$FORMAT_JSON")
    }.toString()
}