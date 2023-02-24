package cz.mendelu.xmusil5.plantmonitor.communication.repositories.plants

import android.graphics.Bitmap
import cz.mendelu.xmusil5.plantmonitor.authentication.IAuthenticationManager
import cz.mendelu.xmusil5.plantmonitor.communication.api.HousePlantMeasurementsApi
import cz.mendelu.xmusil5.plantmonitor.communication.utils.BaseApiRepository
import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.GetPlant
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.PostPlant
import javax.inject.Inject

class PlantsRepositoryImpl @Inject constructor(
    authenticationManager: IAuthenticationManager,
    private val api: HousePlantMeasurementsApi
): BaseApiRepository(authenticationManager), IPlantsRepository {

    override suspend fun getAllPlants(): CommunicationResult<List<GetPlant>> {
        return processRequest{
            api.getAllPlants(
                userId = authenticationManager.getUserId(),
                bearerToken = authenticationManager.getToken()
            )
        }
    }

    override suspend fun getPlantById(plantId: Long): CommunicationResult<GetPlant> {
        return processRequest {
            api.getPlantById(
                id = plantId,
                bearerToken = authenticationManager.getToken()
            )
        }
    }

    override suspend fun getPlantImage(plantId: Long): CommunicationResult<Bitmap> {
        return processImageRequest{
            api.getPlantImage(
                plantId = plantId,
                bearerToken = authenticationManager.getToken()
            )
        }
    }

    override suspend fun postNewPlant(postPlant: PostPlant): CommunicationResult<GetPlant> {
        return processRequest {
            api.postNewPlant(
                postPlant = postPlant,
                bearerToken = authenticationManager.getToken()
            )
        }
    }
}