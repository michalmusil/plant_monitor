package cz.mendelu.xmusil5.plantmonitor.database.daos

import androidx.room.*
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.Plant
import cz.mendelu.xmusil5.plantmonitor.models.database.entities.DbMeasurement
import cz.mendelu.xmusil5.plantmonitor.models.database.entities.DbPlant
import kotlinx.coroutines.flow.Flow

@Dao
interface MeasurementsDao {
    @Query("SELECT * FROM Measurements WHERE plantId = :plantId AND taken >= :fromMillis AND taken <= :toMillis")
    suspend fun getByPlantIdInTimeRange(plantId: Long, fromMillis: Long, toMillis: Long): List<DbMeasurement>

    @Query("SELECT * FROM Measurements WHERE plantId = :plantId ORDER BY taken DESC LIMIT 1")
    suspend fun getLatestByPlantId(plantId: Long): DbMeasurement

    @Insert
    suspend fun insert(measurement: DbMeasurement): Long
    @Update
    suspend fun update(measurement: DbMeasurement)
    @Delete
    suspend fun delete(measurement: DbMeasurement)
}