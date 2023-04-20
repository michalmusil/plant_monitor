package cz.mendelu.xmusil5.plantmonitor.database.daos

import androidx.room.*
import cz.mendelu.xmusil5.plantmonitor.models.database.entities.DbDevice

@Dao
interface DevicesDao {
    @Query("SELECT * FROM Devices")
    suspend fun getAll(): List<DbDevice>

    @Query("SELECT * FROM Devices WHERE id = :deviceId")
    suspend fun getById(deviceId: Long): DbDevice

    @Insert
    suspend fun insert(device: DbDevice): Long
    @Update
    suspend fun update(device: DbDevice)
    @Delete
    suspend fun delete(device: DbDevice)
}