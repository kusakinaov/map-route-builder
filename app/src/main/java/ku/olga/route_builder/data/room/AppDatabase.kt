package ku.olga.route_builder.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import ku.olga.route_builder.data.room.dao.UserPointDao
import ku.olga.route_builder.data.room.entity.UserPoint

@Database(entities = arrayOf(UserPoint::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userPointDao(): UserPointDao
}