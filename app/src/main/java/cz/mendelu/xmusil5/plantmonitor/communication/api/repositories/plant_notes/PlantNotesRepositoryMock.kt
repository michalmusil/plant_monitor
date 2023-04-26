package cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.plant_notes

import cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.plants.PlantsRepositoryMock
import cz.mendelu.xmusil5.plantmonitor.communication.utils.DataError
import cz.mendelu.xmusil5.plantmonitor.communication.utils.DataResult
import cz.mendelu.xmusil5.plantmonitor.models.api.plant_note.PlantNote
import cz.mendelu.xmusil5.plantmonitor.models.api.plant_note.PostPlantNote
import cz.mendelu.xmusil5.plantmonitor.models.api.utils.DateTimeFromApi
import java.util.*

class PlantNotesRepositoryMock: IPlantNotesRepository{
    companion object {
        val PLANT_NOTES = mutableListOf(
            PlantNote(
                id = 1,
                text = "Just planted. Will need more thorough watering for the first month",
                plantId = 1,
                created = DateTimeFromApi(
                    originalString = "",
                    calendarInUTC0 = Calendar.getInstance()
                ),
            ),
            PlantNote(
                id = 2,
                text = "Yellow spots started to show up after too much watering.",
                plantId = 1,
                created = DateTimeFromApi(
                    originalString = "",
                    calendarInUTC0 = Calendar.getInstance()
                ),
            ),
            PlantNote(
                id = 3,
                text = "Needs more attention because of the weak roots",
                plantId = 1,
                created = DateTimeFromApi(
                    originalString = "",
                    calendarInUTC0 = Calendar.getInstance()
                ),
            ),
            PlantNote(
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

    override suspend fun getByPlantId(plantId: Long): DataResult<List<PlantNote>> {
        val matchingNotes =  PLANT_NOTES.filter {
            it.plantId == plantId
        }
        return DataResult.Success(data = matchingNotes)
    }

    override suspend fun addNewPlantNote(postPlantNote: PostPlantNote): DataResult<PlantNote> {
        val existingPlant = PlantsRepositoryMock.PLANTS.firstOrNull { it.id == postPlantNote.plantId }
        existingPlant?.let {
            return DataResult.Error(
                error = DataError(
                    code = 404,
                    message = "No plant with this id"
                )
            )
        }
        val newPlantNote = PlantNote(
            id = (PLANT_NOTES.maxOf{ it.id } + 1),
            text = postPlantNote.text,
            plantId = postPlantNote.plantId,
            created = DateTimeFromApi(
                originalString = "",
                calendarInUTC0 = Calendar.getInstance()
            )
        )
        PLANT_NOTES.add(newPlantNote)
        return DataResult.Success(data = newPlantNote)
    }

    override suspend fun deletePlantNote(plantNoteId: Long): DataResult<Unit> {
        val matching = PLANT_NOTES.firstOrNull { it.id == plantNoteId }
        matching?.let {
            PLANT_NOTES.remove(matching)
            return DataResult.Success(data = Unit)
        }
        return DataResult.Error(
            error = DataError(
                code = 404,
                message = "No note with this id"
            )
        )
    }
}
