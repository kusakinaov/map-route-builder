package ku.olga.nominatim

const val QUERY = "q"
const val FORMAT = "format"
const val FORMAT_JSON = "json"
const val ADDRESS_DETAILS = "addressdetails"
const val EXTRA_TAGS = "extratags"
const val NAME_DETAILS = "namedetails"
const val LIMIT = "limit"
const val MAX_LIMIT = 50
const val ACCEPT_LANGUAGE = "accept-language"
const val LATITUDE = "lat"
const val LONGITUDE = "lon"
const val ZOOM = "zoom"
const val OSM_IDS = "osm_ids"

/**
 * <x1>,<y1>,<x2>,<y2> The preferred area to find search results. Any two corner points of the box are accepted in any order as long as they span a real box. x is longitude, y is latitude.
 */
const val VIEWBOX = "viewbox"

/**
 * When viewbox and bounded=1 are given, an amenity only search is allowed. In this case, give the special keyword for the amenity in square brackets, e.g. [pub]. (Default: 0)
 */
const val BOUNDED = "bounded"
const val AMENITY = "amenity"
const val DEBUG = "debug" //[0|1]

//street=<housenumber> <streetname>
//city=<city>
//county=<county>
//state=<state>
//country=<country>
//postalcode=<postalcode>
//json_callback=<string>
//countrycodes=<countrycode>[,<countrycode>][,<countrycode>]...
//exclude_place_ids=<place_id,[place_id],[place_id]
//polygon_geojson=1
//polygon_kml=1
//polygon_svg=1
//polygon_text=1
//polygon_threshold=0.0
//email=<valid email address>
//dedupe=[0|1]
