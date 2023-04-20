package cz.mendelu.xmusil5.plantmonitor.database.daos

import androidx.room.*
import cz.mendelu.xmusil5.plantmonitor.models.database.entities.DbPlant
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantsDao {
    @Query("SELECT * FROM Plants")
    fun getAll(): Flow<List<DbPlant>>

    @Query("SELECT * FROM Plants WHERE id = :plantId")
    fun getById(plantId: Long): Flow<DbPlant?>

    @Insert
    suspend fun insert(plant: DbPlant): Long
    @Update
    suspend fun update(plant: DbPlant)
    @Delete
    suspend fun delete(plant: DbPlant)
}