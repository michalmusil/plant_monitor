package cz.mendelu.xmusil5.plantmonitor.communication.repositories.plants

import android.graphics.Bitmap
import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.GetPlant

interface IPlantsRepository {
    suspend fun getAllPlants(): CommunicationResult<List<GetPlant>>

    suspend fun getPlantImage(plantId: Long): CommunicationResult<Bitmap>
}