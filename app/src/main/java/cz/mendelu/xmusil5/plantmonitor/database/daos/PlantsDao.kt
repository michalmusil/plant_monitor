package cz.mendelu.xmusil5.plantmonitor.database.daos

import androidx.room.Dao
import androidx.room.Query
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.Plant
import cz.mendelu.xmusil5.plantmonitor.models.database.entities.DbPlant
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantsDao {
    @Query("SELECT * FROM Plants")
    fun getAll(): Flow<List<DbPlant>>
}