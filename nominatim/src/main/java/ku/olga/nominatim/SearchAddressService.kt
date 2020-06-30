package ku.olga.nominatim

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import ku.olga.nominatim.model.Address
import ku.olga.nominatim.model.BoundingBox
import java.net.URL

object SearchAddressService {
    private val gson = GsonBuilder().create()
    private val URL_SEARCH = "https://nominatim.openstreetmap.org/search/" //<query>?<params>

    fun searchByQuery(query: String, boundingBox: BoundingBox?): List<Address> {
        val addresses = mutableListOf<Address>()

        val result = URL(
            URL_SEARCH + query +
                    "?format=json" +
                    "&addressdetails=1" +
                    "&namedetails=1" +
                    "&extratags=1" +
                    "&accept-language=ru"
        ).readText()
        addresses.addAll(gson.fromJson(result, object : TypeToken<List<Address>>() {}.type))

        return addresses
    }
}