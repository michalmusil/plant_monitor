package cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.plant_notes

import cz.mendelu.xmusil5.plantmonitor.authentication.IAuthenticationManager
import cz.mendelu.xmusil5.plantmonitor.communication.api.HousePlantMeasurementsApi
import cz.mendelu.xmusil5.plantmonitor.communication.utils.BaseApiRepository
import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.api.plant_note.GetPlantNote
import cz.mendelu.xmusil5.plantmonitor.models.api.plant_note.PostPlantNote
import javax.inject.Inject

class PlantNotesRepositoryImpl @Inject constructor(
    authenticationManager: IAuthenticationManager,
    private val api: HousePlantMeasurementsApi
): BaseApiRepository(authenticationManager), IPlantNotesRepository {

    override suspend fun getByPlantId(plantId: Long): CommunicationResult<List<GetPlantNote>> {
        return processRequest {
            api.getNotesOfPlant(
                plantId = plantId,
                bearerToken = authenticationManager.getToken()
            )
        }
    }

    override suspend fun postNewPlantNote(postPlantNote: PostPlantNote): CommunicationResult<GetPlantNote> {
        return processRequest {
            api.postNewPlantNote(
                postPlantNote = postPlantNote,
                bearerToken = authenticationManager.getToken()
            )
        }
    }

    override suspend fun deletePlantNote(plantNoteId: Long): CommunicationResult<Unit> {
        return processRequest {
            api.deletePlantNote(
                plantNoteId = plantNoteId,
                bearerToken = authenticationManager.getToken()
            )
        }
    }
}