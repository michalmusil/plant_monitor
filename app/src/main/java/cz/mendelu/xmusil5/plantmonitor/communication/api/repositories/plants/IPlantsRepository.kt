package cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.plants

import android.graphics.Bitmap
import cz.mendelu.xmusil5.plantmonitor.communication.utils.DataResult
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.Plant
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.PostPlant
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.PutPlant
import cz.mendelu.xmusil5.plantmonitor.utils.image.ImageQuality
import okhttp3.MultipartBody

interface IPlantsRepository {
    suspend fun getAllPlants(): DataResult<List<Plant>>

    suspend fun getPlantById(plantId: Long): DataResult<Plant>

    suspend fun getPlantImage(plantId: Long, imageQuality: ImageQuality): DataResult<Bitmap>

    suspend fun uploadPlantImage(plantId: Long, imagePart: MultipartBody.Part): DataResult<Plant>

    suspend fun postNewPlant(postPlant: PostPlant): DataResult<Plant>

    suspend fun updatePlant(putPlant: PutPlant): DataResult<Plant>

    suspend fun deletePlant(plantId: Long): DataResult<Unit>
}