package ku.olga.route_builder.domain.model

data class UserPoint(
        var id: Long? = null,
        var title: String? = null,
        val lat: Double,
        val lon: Double,
        var description: String? = null
)