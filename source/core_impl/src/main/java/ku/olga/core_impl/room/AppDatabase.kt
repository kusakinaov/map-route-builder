package ku.olga.core_impl.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ku.olga.core_api.database.AppDatabaseContract
import ku.olga.core_api.database.entity.UserPoint

@Database(entities = arrayOf(UserPoint::class), version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase(), AppDatabaseContract