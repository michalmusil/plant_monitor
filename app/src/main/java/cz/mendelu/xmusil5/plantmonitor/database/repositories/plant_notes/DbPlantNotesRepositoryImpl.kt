package cz.mendelu.xmusil5.plantmonitor.database.repositories.plant_notes

import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.database.daos.PlantNotesDao
import cz.mendelu.xmusil5.plantmonitor.database.repositories.BaseDbRepository
import cz.mendelu.xmusil5.plantmonitor.models.database.entities.DbPlantNote
import kotlinx.coroutines.flow.Flow

class DbPlantNotesRepositoryImpl(
    private val plantNotesDao: PlantNotesDao
): BaseDbRepository(), IDbPlantNotesRepository {
    override suspend fun getByPlantId(plantId: Long): CommunicationResult<List<DbPlantNote>> {
        return processDbCall {
            plantNotesDao.getByPlantId(plantId)
        }
    }

    override suspend fun addPlantNote(plantNote: DbPlantNote): CommunicationResult<DbPlantNote> {
        val result = processDbCall {
            plantNotesDao.insert(plantNote)
        }
        return when (result){
            is CommunicationResult.Success -> {
                CommunicationResult.Success(plantNote)
            }
            is CommunicationResult.Error -> {
                CommunicationResult.Error(error = result.error)
            }
            is CommunicationResult.Exception -> {
                CommunicationResult.Exception(exception = result.exception)
            }
        }
    }

    override suspend fun deletePlantNote(plantNote: DbPlantNote): CommunicationResult<Unit> {
        return processDbCall {
            plantNotesDao.delete(plantNote)
        }
    }

}