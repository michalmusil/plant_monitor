package cz.mendelu.xmusil5.plantmonitor.communication.repositories.measurements

import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.GetMeasurement
import retrofit2.Response

interface IMeasurementsRepository {
    suspend fun getMeasurementsOfPlant(plantId: Long): CommunicationResult<List<GetMeasurement>>

    suspend fun getMeasurementsOfDevice(deviceId: Long): CommunicationResult<List<GetMeasurement>>
}