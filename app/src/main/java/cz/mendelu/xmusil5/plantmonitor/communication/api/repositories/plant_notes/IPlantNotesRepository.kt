package cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.plant_notes

import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.api.plant_note.PlantNote
import cz.mendelu.xmusil5.plantmonitor.models.api.plant_note.PostPlantNote

interface IPlantNotesRepository {

    suspend fun getByPlantId(plantId: Long): CommunicationResult<List<PlantNote>>

    suspend fun addNewPlantNote(postPlantNote: PostPlantNote): CommunicationResult<PlantNote>

    suspend fun deletePlantNote(plantNoteId: Long): CommunicationResult<Unit>
}