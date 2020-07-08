package ku.olga.nominatim

import ku.olga.nominatim.model.BoundingBox
import ku.olga.nominatim.model.Place
import ku.olga.nominatim.model.ZoomLevel
import java.util.*

object NominatimHelper {
    fun searchPOI(
        query: String? = null,
        boundingBox: BoundingBox? = null,
        amenityTag: String? = null,
        language: String = Locale.getDefault().language,
        limit: Int = MAX_LIMIT,
        debug: Boolean = false
    ): List<Place> = SearchService.search(query, boundingBox, amenityTag, language, limit, debug)

    fun reverseSearchPOI(
        latitude: Double,
        longitude: Double,
        zoom: Int = ZoomLevel.building.value,
        language: String = Locale.getDefault().language,
        debug: Boolean = false
    ): List<Place> = ReverseSearchService.reverseSearch(latitude, longitude, zoom, language, debug)
}