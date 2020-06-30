package ku.olga.core_impl.repository.model

import java.io.Serializable

data class Tag(
    val word: String,
    val key: String,
    val value: String,
    val operator: Operator,
    val plural: Plural
) : Serializable