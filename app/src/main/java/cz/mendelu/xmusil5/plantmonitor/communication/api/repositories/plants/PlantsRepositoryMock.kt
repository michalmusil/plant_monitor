package cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.plants

import android.graphics.Bitmap
import cz.mendelu.xmusil5.plantmonitor.user_session.UserSessionManagerMock
import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationError
import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementType
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementValueLimit
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.Plant
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.PostPlant
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.PutPlant
import cz.mendelu.xmusil5.plantmonitor.models.api.utils.DateTimeFromApi
import cz.mendelu.xmusil5.plantmonitor.utils.image.ImageQuality
import okhttp3.MultipartBody
import java.util.*

class PlantsRepositoryMock: IPlantsRepository {

    companion object{
        val PLANTS = listOf(
            Plant(
                id = 1,
                userId = UserSessionManagerMock.MOCKED_USER_ID,
                name = "Aloe vera - living room",
                species = "Succulent",
                description = "Had this plant since 2018",
                hasTitleImage = true,
                created = DateTimeFromApi(
                    originalString = "",
                    calendarInUTC0 = Calendar.getInstance()
                ),
                valueLimits = listOf(
                    MeasurementValueLimit(
                        type = MeasurementType.LIGHT_INTENSITY,
                        lowerLimit = 0.0,
                        upperLimit = 1200.0
                    ),
                    MeasurementValueLimit(
                        type = MeasurementType.TEMPERATURE,
                        lowerLimit = 15.0,
                        upperLimit = 35.0
                    ),
                    MeasurementValueLimit(
                        type = MeasurementType.SOIL_MOISTURE,
                        lowerLimit = 10.0,
                        upperLimit = 80.0
                    )
                )
            ),
            Plant(
                id = 2,
                userId = UserSessionManagerMock.MOCKED_USER_ID,
                name = "Sempervivum - gray one",
                species = "Succulent",
                description = "Found it growing naturally in the garden",
                hasTitleImage = true,
                created = DateTimeFromApi(
                    originalString = "",
                    calendarInUTC0 = Calendar.getInstance()
                ),
                valueLimits = listOf(
                    MeasurementValueLimit(
                        type = MeasurementType.LIGHT_INTENSITY,
                        lowerLimit = 0.0,
                        upperLimit = 1800.0
                    ),
                    MeasurementValueLimit(
                        type = MeasurementType.SOIL_MOISTURE,
                        lowerLimit = 0.0,
                        upperLimit = 80.0
                    )
                )
            ),
            Plant(
                id = 3,
                userId = UserSessionManagerMock.MOCKED_USER_ID,
                name = "Yucca",
                species = "Palm",
                description = null,
                hasTitleImage = false,
                created = DateTimeFromApi(
                    originalString = "",
                    calendarInUTC0 = Calendar.getInstance()
                ),
                valueLimits = listOf(
                    MeasurementValueLimit(
                        type = MeasurementType.TEMPERATURE,
                        lowerLimit = 15.0,
                        upperLimit = 43.0
                    )
                )
            ),
            Plant(
                id = 4,
                userId = UserSessionManagerMock.MOCKED_USER_ID,
                name = "Rhipsalis",
                species = "Succulent/cacti",
                description = null,
                hasTitleImage = false,
                created = DateTimeFromApi(
                    originalString = "",
                    calendarInUTC0 = Calendar.getInstance()
                ),
                valueLimits = listOf()
            )
        )
    }


    override suspend fun getAllPlants(): CommunicationResult<List<Plant>> {
        return CommunicationResult.Success(data = PLANTS)
    }

    override suspend fun getPlantById(plantId: Long): CommunicationResult<Plant> {
        val foundPlant = PLANTS.firstOrNull{
            it.id == plantId
        }
        foundPlant?.let {
            return CommunicationResult.Success(data = it)
        }
        return CommunicationResult.Error(
            error = CommunicationError(
                code = 404,
                message = "Plant not found"
            )
        )
    }

    override suspend fun getPlantImage(plantId: Long, imageQuality: ImageQuality): CommunicationResult<Bitmap> {
        return CommunicationResult.Error(
            error = CommunicationError(
                code = 404,
                message = "Image not found"
            )
        )
    }

    override suspend fun uploadPlantImage(
        plantId: Long,
        imagePart: MultipartBody.Part
    ): CommunicationResult<Plant> {
        val plant = PLANTS.firstOrNull {
            it.id == plantId
        }
        plant?.let {
            return CommunicationResult.Success(data = it)
        }
        return CommunicationResult.Error(
            error = CommunicationError(
                code = 404,
                message = "Plant not found"
            )
        )
    }

    override suspend fun postNewPlant(postPlant: PostPlant): CommunicationResult<Plant> {
        val maxId = PLANTS.maxOf { it.id }
        val newPlant = Plant(
            id = maxId + 1,
            userId = UserSessionManagerMock.MOCKED_USER_ID,
            name = postPlant.name,
            species = postPlant.species,
            description = postPlant.description,
            hasTitleImage = false,
            created = DateTimeFromApi(
                originalString = "",
                calendarInUTC0 = Calendar.getInstance()
            ),
            valueLimits = postPlant.measurementValueLimits ?: listOf()
        )
        return CommunicationResult.Success(data = newPlant)
    }

    override suspend fun updatePlant(putPlant: PutPlant): CommunicationResult<Plant> {
        val updatedPlant = Plant(
            id = putPlant.id,
            userId = UserSessionManagerMock.MOCKED_USER_ID,
            name = putPlant.name,
            species = putPlant.species,
            description = putPlant.description,
            hasTitleImage = false,
            created = DateTimeFromApi(
                originalString = "",
                calendarInUTC0 = Calendar.getInstance()
            ),
            valueLimits = putPlant.measurementValueLimits ?: listOf()
        )
        return CommunicationResult.Success(data = updatedPlant)
    }

    override suspend fun deletePlant(plantId: Long): CommunicationResult<Unit> {
        return CommunicationResult.Success(data = Unit)
    }
}