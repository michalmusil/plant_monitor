package cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.measurements

import cz.mendelu.xmusil5.plantmonitor.communication.utils.DataResult
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.Measurement
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.LatestMeasurementValueOfPlant
import java.util.Calendar

interface IMeasurementsRepository {
    suspend fun getMeasurementsOfPlant(plantId: Long, from: Calendar?, to: Calendar?): DataResult<List<Measurement>>

    suspend fun getLatestPlantMeasurementValues(plantId: Long): DataResult<List<LatestMeasurementValueOfPlant>>
}