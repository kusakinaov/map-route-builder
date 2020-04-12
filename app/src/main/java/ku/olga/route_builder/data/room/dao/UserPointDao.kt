package ku.olga.route_builder.data.room.dao

import androidx.room.*
import ku.olga.route_builder.data.room.entity.UserPoint

@Dao
interface UserPointDao {
    @Query("SELECT * FROM user_point")
    suspend fun getAll(): List<UserPoint>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(userPoint: UserPoint): Long

    @Query("SELECT max(id) FROM user_point")
    suspend fun getMaxIdx(): Long

    @Delete
    suspend fun delete(userPoint: UserPoint): Int
}