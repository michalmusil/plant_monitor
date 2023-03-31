package cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.plant_notes

import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.api.plant_note.GetPlantNote
import cz.mendelu.xmusil5.plantmonitor.models.api.plant_note.PostPlantNote

interface IPlantNotesRepository {

    suspend fun getByPlantId(plantId: Long): CommunicationResult<List<GetPlantNote>>

    suspend fun postNewPlantNote(postPlantNote: PostPlantNote): CommunicationResult<GetPlantNote>

    suspend fun deletePlantNote(plantNoteId: Long): CommunicationResult<Unit>
}