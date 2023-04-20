package cz.mendelu.xmusil5.plantmonitor.database.daos

import androidx.room.*
import cz.mendelu.xmusil5.plantmonitor.models.database.entities.DbPlant
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantsDao {
    @Query("SELECT * FROM Plants")
    suspend fun getAll(): List<DbPlant>

    @Query("SELECT * FROM Plants WHERE id = :plantId")
    suspend fun getById(plantId: Long): DbPlant

    @Insert
    suspend fun insert(plant: DbPlant): Long
    @Update
    suspend fun update(plant: DbPlant)
    @Delete
    suspend fun delete(plant: DbPlant)
}