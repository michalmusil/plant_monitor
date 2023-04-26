package cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.plant_notes

import cz.mendelu.xmusil5.plantmonitor.communication.utils.DataResult
import cz.mendelu.xmusil5.plantmonitor.models.api.plant_note.PlantNote
import cz.mendelu.xmusil5.plantmonitor.models.api.plant_note.PostPlantNote

interface IPlantNotesRepository {

    suspend fun getByPlantId(plantId: Long): DataResult<List<PlantNote>>

    suspend fun addNewPlantNote(postPlantNote: PostPlantNote): DataResult<PlantNote>

    suspend fun deletePlantNote(plantNoteId: Long): DataResult<Unit>
}