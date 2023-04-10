package cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.plants

import android.graphics.Bitmap
import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.Plant
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.PostPlant
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.PutPlant
import cz.mendelu.xmusil5.plantmonitor.utils.image.ImageQuality
import okhttp3.MultipartBody

interface IPlantsRepository {
    suspend fun getAllPlants(): CommunicationResult<List<Plant>>

    suspend fun getPlantById(plantId: Long): CommunicationResult<Plant>

    suspend fun getPlantImage(plantId: Long, imageQuality: ImageQuality = ImageQuality.SMALL): CommunicationResult<Bitmap>

    suspend fun uploadPlantImage(plantId: Long, imagePart: MultipartBody.Part): CommunicationResult<Plant>

    suspend fun postNewPlant(postPlant: PostPlant): CommunicationResult<Plant>

    suspend fun updatePlant(putPlant: PutPlant): CommunicationResult<Plant>

    suspend fun deletePlant(plantId: Long): CommunicationResult<Unit>
}