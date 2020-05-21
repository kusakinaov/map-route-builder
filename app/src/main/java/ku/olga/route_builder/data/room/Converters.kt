package ku.olga.route_builder.data.room

import androidx.room.TypeConverter
import ku.olga.route_builder.domain.model.UserPointType

class Converters {
    @TypeConverter
    fun fromUserPointType(userPointType: UserPointType) = userPointType.name

    @TypeConverter
    fun toUserPointType(name: String) = UserPointType.valueOf(name)
}