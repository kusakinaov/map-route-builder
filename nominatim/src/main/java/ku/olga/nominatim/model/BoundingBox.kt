package ku.olga.nominatim.model

import java.io.Serializable

data class BoundingBox(
    val lat1: Double, val lon1: Double,
    val lat2: Double, val lon2: Double
) : Serializable