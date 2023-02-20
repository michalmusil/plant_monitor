package cz.mendelu.xmusil5.plantmonitor.communication.repositories.plants

import android.graphics.Bitmap
import cz.mendelu.xmusil5.plantmonitor.authentication.IAuthenticationManager
import cz.mendelu.xmusil5.plantmonitor.communication.api.HousePlantMeasurementsApi
import cz.mendelu.xmusil5.plantmonitor.communication.utils.BaseApiRepository
import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.GetPlant
import javax.inject.Inject

class PlantsRepositoryImpl @Inject constructor(
    authenticationManager: IAuthenticationManager,
    private val api: HousePlantMeasurementsApi
): BaseApiRepository(authenticationManager), IPlantsRepository {

    override suspend fun getAllPlants(): CommunicationResult<List<GetPlant>> {
        val call = api.getAllPlants(
            userId = authenticationManager.getUserId(),
            bearerToken = authenticationManager.getToken()
        )
        return processResponse(call)
    }

    override suspend fun getPlantImage(plantId: Long): CommunicationResult<Bitmap> {
        val call = api.getPlantImage(
            plantId = plantId,
            bearerToken = authenticationManager.getToken()
        )
        return processImageResponse(call)
    }
}