package cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.plant_notes

import cz.mendelu.xmusil5.plantmonitor.user_session.IUserSessionManager
import cz.mendelu.xmusil5.plantmonitor.communication.api.HousePlantMeasurementsApi
import cz.mendelu.xmusil5.plantmonitor.communication.utils.BaseApiRepository
import cz.mendelu.xmusil5.plantmonitor.communication.utils.DataResult
import cz.mendelu.xmusil5.plantmonitor.models.api.plant_note.PlantNote
import cz.mendelu.xmusil5.plantmonitor.models.api.plant_note.PostPlantNote
import javax.inject.Inject

class PlantNotesRepositoryImpl @Inject constructor(
    userSessionManager: IUserSessionManager,
    private val api: HousePlantMeasurementsApi
): BaseApiRepository(userSessionManager), IPlantNotesRepository {

    override suspend fun getByPlantId(plantId: Long): DataResult<List<PlantNote>> {
        return processRequest {
            api.getNotesOfPlant(
                plantId = plantId,
                bearerToken = userSessionManager.getToken()
            )
        }
    }

    override suspend fun addNewPlantNote(postPlantNote: PostPlantNote): DataResult<PlantNote> {
        return processRequest {
            api.postNewPlantNote(
                postPlantNote = postPlantNote,
                bearerToken = userSessionManager.getToken()
            )
        }
    }

    override suspend fun deletePlantNote(plantNoteId: Long): DataResult<Unit> {
        return processRequest {
            api.deletePlantNote(
                plantNoteId = plantNoteId,
                bearerToken = userSessionManager.getToken()
            )
        }
    }
}