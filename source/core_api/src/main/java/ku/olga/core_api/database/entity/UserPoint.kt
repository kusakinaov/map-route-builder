package ku.olga.core_api.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ku.olga.core_api.dto.UserPointType

@Entity(tableName = "user_point")
data class UserPoint(
    @PrimaryKey var id: Long? = null,
    @ColumnInfo(name = "title") var title: String? = null,
    @ColumnInfo(name = "postal_address") val postalAddress: String? = null,
    @ColumnInfo(name = "lat") val lat: Double,
    @ColumnInfo(name = "lon") val lon: Double,
    @ColumnInfo(name = "description") var description: String? = null,
    @ColumnInfo(name = "type") val type: UserPointType,
    @ColumnInfo(name = "order") val order: Int
)