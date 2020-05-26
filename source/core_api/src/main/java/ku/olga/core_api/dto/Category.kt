package ku.olga.core_api.dto

import java.io.Serializable

data class Category(val key: String, val title: String, val description: String? = null) :
    Serializable