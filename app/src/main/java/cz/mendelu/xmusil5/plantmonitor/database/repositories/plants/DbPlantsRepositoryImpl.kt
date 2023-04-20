package cz.mendelu.xmusil5.plantmonitor.database.repositories.plants

import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.database.daos.PlantsDao
import cz.mendelu.xmusil5.plantmonitor.database.repositories.BaseDbRepository
import cz.mendelu.xmusil5.plantmonitor.models.database.entities.DbPlant
import kotlinx.coroutines.flow.Flow

class DbPlantsRepositoryImpl(
    private val plantsDao: PlantsDao
    ): BaseDbRepository(), IDbPlantsRepository {
    override suspend fun getAllPlants(): CommunicationResult<Flow<List<DbPlant>>> {
        return processDbCall {
            plantsDao.getAll()
        }
    }

    override suspend fun getPlantById(plantId: Long): CommunicationResult<Flow<DbPlant?>> {
        return processDbCall {
            plantsDao.getById(plantId)
        }
    }

    override suspend fun addPlant(plant: DbPlant): CommunicationResult<DbPlant> {
        val result = processDbCall {
            plantsDao.insert(plant)
        }
        return when (result){
            is CommunicationResult.Success -> {
                CommunicationResult.Success(plant)
            }
            is CommunicationResult.Error -> {
                CommunicationResult.Error(error = result.error)
            }
            is CommunicationResult.Exception -> {
                CommunicationResult.Exception(exception = result.exception)
            }
        }
    }

    override suspend fun updatePlant(plant: DbPlant): CommunicationResult<DbPlant> {
        val result = processDbCall {
            plantsDao.insert(plant)
        }
        return when (result){
            is CommunicationResult.Success -> {
                CommunicationResult.Success(plant)
            }
            is CommunicationResult.Error -> {
                CommunicationResult.Error(error = result.error)
            }
            is CommunicationResult.Exception -> {
                CommunicationResult.Exception(exception = result.exception)
            }
        }
    }

    override suspend fun deletePlant(plant: DbPlant): CommunicationResult<Unit> {
        return processDbCall {
            plantsDao.delete(plant)
        }
    }
}