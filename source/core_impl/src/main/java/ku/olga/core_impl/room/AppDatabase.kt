package ku.olga.core_impl.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ku.olga.core_api.database.AppDatabaseContract
import ku.olga.core_api.database.UserPointDao
import ku.olga.core_api.database.entity.UserPoint
import ku.olga.core_impl.room.Converters

@Database(entities = arrayOf(UserPoint::class), version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase(),AppDatabaseContract