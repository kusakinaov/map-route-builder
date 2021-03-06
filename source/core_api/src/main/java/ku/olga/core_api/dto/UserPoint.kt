package ku.olga.core_api.dto

import java.io.Serializable

data class UserPoint(
        var id: Long? = null,
        var title: String? = null,
        val postalAddress: String? = null,
        val lat: Double,
        val lon: Double,
        var description: String? = null,
        val type: UserPointType,
        var order: Int = 0
) : Serializable {
    val isNew: Boolean
        get() = id == null
}