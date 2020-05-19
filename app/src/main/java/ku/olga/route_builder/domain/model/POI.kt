package ku.olga.route_builder.domain.model

data class POI(val id: Long,
    val latitude: Double,
    val longitude: Double,
    val title: String,
    val description: String?,
    val thumbnailPath: String?,
    val url: String?,
    val rank: Int,
    val categoryGroup: String? = null,
    val category: String? = null)