package cz.mendelu.xmusil5.plantmonitor.communication.repositories.measurements

import cz.mendelu.xmusil5.plantmonitor.authentication.IAuthenticationManager
import cz.mendelu.xmusil5.plantmonitor.communication.api.HousePlantMeasurementsApi
import cz.mendelu.xmusil5.plantmonitor.communication.repositories.plants.IPlantsRepository
import cz.mendelu.xmusil5.plantmonitor.communication.utils.BaseApiRepository
import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.GetMeasurement
import javax.inject.Inject

class MeasurementsRepositoryImpl @Inject constructor(
    authenticationManager: IAuthenticationManager,
    private val api: HousePlantMeasurementsApi
): BaseApiRepository(authenticationManager), IMeasurementsRepository {

    override suspend fun getMeasurementsOfPlant(plantId: Long): CommunicationResult<List<GetMeasurement>> {
        val call = api.getMeasurementsOfPlant(
            plantId = plantId,
            bearerToken = authenticationManager.getToken()
        )
        return processResponse(call)
    }

    override suspend fun getMeasurementsOfDevice(deviceId: Long): CommunicationResult<List<GetMeasurement>> {
        val call = api.getMeasurementsOfDevice(
            deviceId = deviceId,
            bearerToken = authenticationManager.getToken()
        )
        return processResponse(call)
    }
}