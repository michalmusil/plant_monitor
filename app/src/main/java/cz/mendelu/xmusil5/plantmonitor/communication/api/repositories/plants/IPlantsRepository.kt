package cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.plants

import android.graphics.Bitmap
import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.GetPlant
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.PostPlant
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.PutPlant
import okhttp3.MultipartBody

interface IPlantsRepository {
    suspend fun getAllPlants(): CommunicationResult<List<GetPlant>>

    suspend fun getPlantById(plantId: Long): CommunicationResult<GetPlant>

    suspend fun getPlantImage(plantId: Long): CommunicationResult<Bitmap>

    suspend fun uploadPlantImage(plantId: Long, imagePart: MultipartBody.Part): CommunicationResult<GetPlant>

    suspend fun postNewPlant(postPlant: PostPlant): CommunicationResult<GetPlant>

    suspend fun updatePlant(putPlant: PutPlant): CommunicationResult<GetPlant>

    suspend fun deletePlant(plantId: Long): CommunicationResult<Unit>
}