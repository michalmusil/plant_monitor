package cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.plants

import android.graphics.Bitmap
import cz.mendelu.xmusil5.plantmonitor.authentication.AuthenticationManagerMock
import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationError
import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementType
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementValueLimit
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.GetPlant
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.PostPlant
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.PutPlant
import cz.mendelu.xmusil5.plantmonitor.models.api.utils.DateTimeFromApi
import okhttp3.MultipartBody
import java.util.*
import kotlin.math.max

class PlantsRepositoryMock: IPlantsRepository {

    companion object{
        val PLANTS = listOf(
            GetPlant(
                id = 1,
                userId = AuthenticationManagerMock.MOCKED_USER_ID,
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
            GetPlant(
                id = 2,
                userId = AuthenticationManagerMock.MOCKED_USER_ID,
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
            GetPlant(
                id = 3,
                userId = AuthenticationManagerMock.MOCKED_USER_ID,
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
            GetPlant(
                id = 4,
                userId = AuthenticationManagerMock.MOCKED_USER_ID,
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


    override suspend fun getAllPlants(): CommunicationResult<List<GetPlant>> {
        return CommunicationResult.Success(data = PLANTS)
    }

    override suspend fun getPlantById(plantId: Long): CommunicationResult<GetPlant> {
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

    override suspend fun getPlantImage(plantId: Long): CommunicationResult<Bitmap> {
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
    ): CommunicationResult<GetPlant> {
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

    override suspend fun postNewPlant(postPlant: PostPlant): CommunicationResult<GetPlant> {
        val maxId = PLANTS.maxOf { it.id }
        val newPlant = GetPlant(
            id = maxId + 1,
            userId = AuthenticationManagerMock.MOCKED_USER_ID,
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

    override suspend fun updatePlant(putPlant: PutPlant): CommunicationResult<GetPlant> {
        val updatedPlant = GetPlant(
            id = putPlant.id,
            userId = AuthenticationManagerMock.MOCKED_USER_ID,
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