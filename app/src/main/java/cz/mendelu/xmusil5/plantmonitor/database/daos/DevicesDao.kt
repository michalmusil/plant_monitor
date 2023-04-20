package cz.mendelu.xmusil5.plantmonitor.database.daos

import androidx.room.*
import cz.mendelu.xmusil5.plantmonitor.models.database.entities.DbDevice
import kotlinx.coroutines.flow.Flow

@Dao
interface DevicesDao {
    @Query("SELECT * FROM Devices")
    fun getAll(): Flow<List<DbDevice>>

    @Query("SELECT * FROM Devices WHERE id = :deviceId")
    fun getById(deviceId: Long): Flow<DbDevice?>

    @Insert
    suspend fun insert(device: DbDevice): Long
    @Update
    suspend fun update(device: DbDevice)
    @Delete
    suspend fun delete(device: DbDevice)
}