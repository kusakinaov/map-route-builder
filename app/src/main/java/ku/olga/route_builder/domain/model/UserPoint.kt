package ku.olga.route_builder.domain.model

import java.io.Serializable

data class UserPoint(
        var id: Long? = null,
        var title: String? = null,
        val postalAddress: String? = null,
        val lat: Double,
        val lon: Double,
        var description: String? = null
) : Serializable {
    val isNew: Boolean
        get() = id == null
}