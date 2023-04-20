package cz.mendelu.xmusil5.plantmonitor.database.daos

import androidx.room.*
import cz.mendelu.xmusil5.plantmonitor.models.database.entities.DbPlantNote
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantNotesDao {
    @Query("SELECT * FROM PlantNotes WHERE plantId = :plantId")
    suspend fun getByPlantId(plantId: Long): List<DbPlantNote>

    @Insert
    suspend fun insert(plantNote: DbPlantNote): Long
    @Update
    suspend fun update(plantNote: DbPlantNote)
    @Delete
    suspend fun delete(plantNote: DbPlantNote)
}