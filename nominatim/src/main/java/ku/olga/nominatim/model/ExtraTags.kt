package ku.olga.nominatim.model

import java.io.Serializable

data class ExtraTags(
    val capital: String,
    val website: String,
    val wikidata: String,
    val wikipedia: String,
    val population: Long
) : Serializable