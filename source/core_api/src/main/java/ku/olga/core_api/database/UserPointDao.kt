package ku.olga.core_api.database

import androidx.room.*
import ku.olga.core_api.database.entity.UserPoint

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