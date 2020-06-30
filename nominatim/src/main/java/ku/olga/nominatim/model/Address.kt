package ku.olga.nominatim.model

import java.io.Serializable

data class Address(
    val continent: String?,

    val country: String?,
    val country_code: String?,

    val region: String?,
    val state: String?,
    val state_district: String?,
    val county: String?,

    val municipality: String?,
    val city: String?,
    val town: String?,
    val village: String?,

    val postcode: String?,

    val city_district: String?,
    val district: String?,
    val borough: String?,
    val suburb: String?,
    val subdivision: String?,

    val hamlet: String?,
    val croft: String?,
    val isolated_dwelling: String?,

    val neighbourhood: String?,
    val allotments: String?,
    val quarter: String?,

    val city_block: String?,
    val residental: String?,
    val farm: String?,
    val farmyard: String?,
    val industrial: String?,
    val commercial: String?,
    val retail: String?,

    val road: String?,

    val house_number: String?,
    val house_name: String?,

    val emergency: String?,
    val historic: String?,
    val military: String?,
    val natural: String?,
    val landuse: String?,
    val place: String?,
    val railway: String?,
    val man_made: String?,
    val aerialway: String?,
    val boundary: String?,
    val amenity: String?,
    val aeroway: String?,
    val club: String?,
    val craft: String?,
    val leisure: String?,
    val office: String?,
    val mountain_pass: String?,
    val shop: String?,
    val tourism: String?,
    val bridge: String?,
    val tunnel: String?,
    val waterway: String?
) : Serializable