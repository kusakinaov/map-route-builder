package ku.olga.nominatim.model

import java.io.Serializable

data class BoundingBox(val southWest: Coordinates, val northEast: Coordinates) : Serializable