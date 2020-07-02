package ku.olga.nominatim

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import ku.olga.nominatim.gson.BoundingBoxTypeAdapter
import ku.olga.nominatim.model.Address
import ku.olga.nominatim.model.BoundingBox
import java.net.URL

object SearchAddressService {
    private val gson = GsonBuilder()
        .registerTypeAdapter(BoundingBox::class.java, BoundingBoxTypeAdapter())
        .create()
    private const val URL_SEARCH = "https://nominatim.openstreetmap.org/search/"

    fun search(
        query: String,
        boundingBox: BoundingBox?,
        amenityTag: String?,
        language: String,
        debug: Boolean = false
    ): List<Address> {
        val addresses = mutableListOf<Address>()

        val result = URL(
            URL_SEARCH + buildQuery(
                query,
                boundingBox,
                boundingBox != null,
                amenityTag,
                language,
                debug
            )
        ).readText()
        addresses.addAll(gson.fromJson(result, object : TypeToken<List<Address>>() {}.type))

        return addresses
    }

    private fun buildQuery(
        query: String? = null,
        boundingBox: BoundingBox? = null,
        bound: Boolean = false,
        amenityTag: String? = null,
        language: String,
        debug: Boolean
    ): String = StringBuilder().apply {
        append("?").append("$ACCEPT_LANGUAGE=$language")
        append("&").append("$DEBUG=${if (debug) 1 else 0}")
        append("&").append("$ADDRESS_DETAILS=1")
        append("&").append("$EXTRA_TAGS=1")
        append("&").append("$NAME_DETAILS=1")
        if (query?.isNotEmpty() == true) {
            append("&").append("$QUERY=$query")
//        } else if (amenityTag?.isNotEmpty() == true) {
//            append("&").append("$AMENITY=$amenityTag")
        }
        boundingBox?.let {
            append("&").append("$VIEWBOX=${it.southWest.latitude},${it.southWest.longitude},${it.northEast.latitude},${it.northEast.longitude}")
            append("&").append("$BOUNDED=${if (bound) 1 else 0}")
            amenityTag?.let { append("&").append("[$amenityTag]") }
        }
    }.toString()
}