package cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.plants

import android.graphics.Bitmap
import cz.mendelu.xmusil5.plantmonitor.user_session.IUserSessionManager
import cz.mendelu.xmusil5.plantmonitor.communication.api.HousePlantMeasurementsApi
import cz.mendelu.xmusil5.plantmonitor.communication.utils.BaseApiRepository
import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.Plant
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.PostPlant
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.PutPlant
import cz.mendelu.xmusil5.plantmonitor.utils.image.ImageQuality
import okhttp3.MultipartBody
import javax.inject.Inject

class PlantsRepositoryImpl @Inject constructor(
    userSessionManager: IUserSessionManager,
    private val api: HousePlantMeasurementsApi
): BaseApiRepository(userSessionManager), IPlantsRepository {

    override suspend fun getAllPlants(): CommunicationResult<List<Plant>> {
        return processRequest{
            api.getAllPlants(
                userId = userSessionManager.getUserId(),
                bearerToken = userSessionManager.getToken()
            )
        }
    }

    override suspend fun getPlantById(plantId: Long): CommunicationResult<Plant> {
        return processRequest {
            api.getPlantById(
                id = plantId,
                bearerToken = userSessionManager.getToken()
            )
        }
    }

    override suspend fun getPlantImage(plantId: Long, imageQuality: ImageQuality): CommunicationResult<Bitmap> {
        return processImageRequest(resultBitmapQuality = imageQuality){
            api.getPlantImage(
                plantId = plantId,
                bearerToken = userSessionManager.getToken()
            )
        }
    }

    override suspend fun uploadPlantImage(
        plantId: Long,
        imagePart: MultipartBody.Part
    ): CommunicationResult<Plant> {
       return processRequest {
           api.uploadPlantImage(
               plantId = plantId,
               image = imagePart,
               bearerToken = userSessionManager.getToken()
           )
       }
    }

    override suspend fun postNewPlant(postPlant: PostPlant): CommunicationResult<Plant> {
        return processRequest {
            api.postNewPlant(
                postPlant = postPlant,
                bearerToken = userSessionManager.getToken()
            )
        }
    }

    override suspend fun updatePlant(putPlant: PutPlant): CommunicationResult<Plant> {
        return processRequest {
            api.updatePlant(
                putPlant = putPlant,
                bearerToken = userSessionManager.getToken()
            )
        }
    }

    override suspend fun deletePlant(plantId: Long): CommunicationResult<Unit> {
        return processRequest {
            api.deletePlant(
                id = plantId,
                bearerToken = userSessionManager.getToken()
            )
        }
    }
}