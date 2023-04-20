package cz.mendelu.xmusil5.plantmonitor.database.repositories.plant_notes

import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.database.entities.DbPlantNote
import kotlinx.coroutines.flow.Flow

interface IDbPlantNotesRepository {
    suspend fun getByPlantId(plantId: Long): CommunicationResult<List<DbPlantNote>>

    suspend fun addPlantNote(plantNote: DbPlantNote): CommunicationResult<DbPlantNote>

    suspend fun deletePlantNote(plantNote: DbPlantNote): CommunicationResult<Unit>
}