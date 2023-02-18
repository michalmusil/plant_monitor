package cz.mendelu.xmusil5.plantmonitor.communication.repositories.plants

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
}