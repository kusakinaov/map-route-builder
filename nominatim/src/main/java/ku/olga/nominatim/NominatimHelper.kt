package ku.olga.nominatim

import ku.olga.nominatim.model.BoundingBox
import ku.olga.nominatim.model.Place
import java.util.*

object NominatimHelper {
    fun searchPOI(query: String? = null,
                  boundingBox: BoundingBox? = null,
                  amenityTag: String? = null,
                  language: String = Locale.getDefault().language,
                  limit: Int = MAX_LIMIT,
                  debug: Boolean = false): List<Place> =
            SearchService.search(query, boundingBox, amenityTag, language, limit, debug)
}