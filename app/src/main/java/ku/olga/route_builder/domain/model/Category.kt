package ku.olga.route_builder.domain.model

import java.io.Serializable

data class Category(val key: String, val title: String, val description: String? = null) :
    Serializable