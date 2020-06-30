package ku.olga.core_impl.room

import androidx.room.TypeConverter
import ku.olga.core_api.dto.UserPointType

class Converters {
    @TypeConverter
    fun fromUserPointType(userPointType: UserPointType): String = userPointType.name

    @TypeConverter
    fun toUserPointType(name: String): UserPointType = UserPointType.valueOf(name)
}