package cz.mendelu.xmusil5.plantmonitor.communication.repositories.measurements

import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.GetMeasurement
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementType
import retrofit2.Response
import java.util.Calendar

interface IMeasurementsRepository {
    suspend fun getMeasurementsOfPlant(plantId: Long, from: Calendar?, to: Calendar?): CommunicationResult<List<GetMeasurement>>

    suspend fun getLatestPlantMeasurementOfType(plantId: Long, measurementType: MeasurementType): CommunicationResult<GetMeasurement>
}