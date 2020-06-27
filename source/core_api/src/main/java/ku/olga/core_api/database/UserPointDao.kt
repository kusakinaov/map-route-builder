package ku.olga.core_api.database

import androidx.room.*
import ku.olga.core_api.database.entity.UserPoint

@Dao
interface UserPointDao {
    @Query("SELECT * FROM user_point ORDER BY `order`")
    suspend fun getAll(): List<UserPoint>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(userPoint: UserPoint): Long

    @Query("SELECT max(id) FROM user_point")
    suspend fun getMaxIdx(): Long

    @Delete
    suspend fun delete(userPoint: UserPoint): Int

    @Query("UPDATE user_point SET `order` = :order WHERE id = :id")
    suspend fun updateOrder(id: Long, order: Int)

    @Transaction
    suspend fun updateOrders(orders: List<UserPoint>) {
        for ((index, value) in orders.withIndex()) {
            value.id?.let { updateOrder(it, index) }
        }
    }
}