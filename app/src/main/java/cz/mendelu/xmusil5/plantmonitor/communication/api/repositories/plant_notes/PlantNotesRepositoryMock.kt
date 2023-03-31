package cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.plant_notes

import cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.plants.PlantsRepositoryMock
import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationError
import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.api.plant_note.GetPlantNote
import cz.mendelu.xmusil5.plantmonitor.models.api.plant_note.PostPlantNote
import cz.mendelu.xmusil5.plantmonitor.models.api.utils.DateTimeFromApi
import java.util.*

class PlantNotesRepositoryMock: IPlantNotesRepository{
    companion object {
        val PLANT_NOTES = mutableListOf(
            GetPlantNote(
                id = 1,
                text = "Just planted. Will need more thorough watering for the first month",
                plantId = 1,
                created = DateTimeFromApi(
                    originalString = "",
                    calendarInUTC0 = Calendar.getInstance()
                ),
            ),
            GetPlantNote(
                id = 2,
                text = "Yellow spots started to show up after too much watering.",
                plantId = 1,
                created = DateTimeFromApi(
                    originalString = "",
                    calendarInUTC0 = Calendar.getInstance()
                ),
            ),
            GetPlantNote(
                id = 3,
                text = "Needs more attention because of the weak roots",
                plantId = 1,
                created = DateTimeFromApi(
                    originalString = "",
                    calendarInUTC0 = Calendar.getInstance()
                ),
            ),
            GetPlantNote(
                id = 4,
                text = "Repoted today.",
                plantId = 3,
                created = DateTimeFromApi(
                    originalString = "",
                    calendarInUTC0 = Calendar.getInstance()
                ),
            ),
        )
    }

    override suspend fun getByPlantId(plantId: Long): CommunicationResult<List<GetPlantNote>> {
        val matchingNotes =  PLANT_NOTES.filter {
            it.plantId == plantId
        }
        return CommunicationResult.Success(data = matchingNotes)
    }

    override suspend fun postNewPlantNote(postPlantNote: PostPlantNote): CommunicationResult<GetPlantNote> {
        val existingPlant = PlantsRepositoryMock.PLANTS.firstOrNull { it.id == postPlantNote.plantId }
        existingPlant?.let {
            return CommunicationResult.Error(
                error = CommunicationError(
                    code = 404,
                    message = "No plant with this id"
                )
            )
        }
        val newPlantNote = GetPlantNote(
            id = (PLANT_NOTES.maxOf{ it.id } + 1),
            text = postPlantNote.text,
            plantId = postPlantNote.plantId,
            created = DateTimeFromApi(
                originalString = "",
                calendarInUTC0 = Calendar.getInstance()
            )
        )
        PLANT_NOTES.add(newPlantNote)
        return CommunicationResult.Success(data = newPlantNote)
    }

    override suspend fun deletePlantNote(plantNoteId: Long): CommunicationResult<Unit> {
        val matching = PLANT_NOTES.firstOrNull { it.id == plantNoteId }
        matching?.let {
            PLANT_NOTES.remove(matching)
            return CommunicationResult.Success(data = Unit)
        }
        return CommunicationResult.Error(
            error = CommunicationError(
                code = 404,
                message = "No note with this id"
            )
        )
    }
}
