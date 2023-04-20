package cz.mendelu.xmusil5.plantmonitor.database.daos

import androidx.room.*
import cz.mendelu.xmusil5.plantmonitor.models.database.entities.DbUser
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersDao {
    @Query("SELECT * FROM Users")
    fun getAll(): Flow<List<DbUser>>

    @Query("SELECT * FROM Users WHERE id = :userId")
    fun getById(userId: Long): Flow<DbUser?>

    @Insert
    suspend fun insert(user: DbUser): Long
    @Update
    suspend fun update(user: DbUser)
    @Delete
    suspend fun delete(user: DbUser)
}