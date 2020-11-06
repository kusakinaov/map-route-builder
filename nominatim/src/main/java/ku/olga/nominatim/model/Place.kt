package ku.olga.nominatim.model

import java.io.Serializable

data class Place(val place_id: Long,
                 val licence: String,
                 val osm_type: OsmType,
                 val osm_id: Long,
                 val boundingbox: BoundingBox?,
                 val lat: Double,
                 val lon: Double,
                 val display_name: String,
                 val `class`: String, //enum
                 val type: String, //enum
                 val importance: Double,
                 val icon: String,
                 val address: Address,
                 val extratags: ExtraTags) : Serializable