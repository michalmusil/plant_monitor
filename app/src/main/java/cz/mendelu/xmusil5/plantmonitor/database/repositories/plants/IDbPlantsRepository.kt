package cz.mendelu.xmusil5.plantmonitor.database.repositories.plants

import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.database.entities.DbPlant
import kotlinx.coroutines.flow.Flow

interface IDbPlantsRepository {
    suspend fun getAllPlants(): CommunicationResult<List<DbPlant>>

    suspend fun getPlantById(plantId: Long): CommunicationResult<DbPlant>

    suspend fun addPlant(plant: DbPlant): CommunicationResult<DbPlant>

    suspend fun updatePlant(plant: DbPlant): CommunicationResult<DbPlant>

    suspend fun deletePlant(plant: DbPlant): CommunicationResult<Unit>

}