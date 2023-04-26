package cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.measurements

import cz.mendelu.xmusil5.plantmonitor.communication.utils.DataResult
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.Measurement
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.LatestMeasurementValueOfPlant
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementType
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementValue
import cz.mendelu.xmusil5.plantmonitor.models.api.utils.DateTimeFromApi
import java.util.*

class MeasurementsRepositoryMock: IMeasurementsRepository {

    companion object{
        val MEASUREMENTS = listOf(
            Measurement(
                id = 1,
                datetime = DateTimeFromApi(
                    originalString = "",
                    calendarInUTC0 = Calendar.getInstance()
                ),
                plantId = 1,
                deviceId = 1,
                values = listOf(
                    MeasurementValue(
                        measurementType = MeasurementType.TEMPERATURE,
                        value = 22.2
                    ),
                    MeasurementValue(
                        measurementType = MeasurementType.LIGHT_INTENSITY,
                        value = 760.0
                    ),
                    MeasurementValue(
                        measurementType = MeasurementType.SOIL_MOISTURE,
                        value = 25.0
                    ),
                )
            ),
            Measurement(
                id = 2,
                datetime = DateTimeFromApi(
                    originalString = "",
                    calendarInUTC0 = Calendar.getInstance()
                ),
                plantId = 1,
                deviceId = 1,
                values = listOf(
                    MeasurementValue(
                        measurementType = MeasurementType.TEMPERATURE,
                        value = 24.32
                    ),
                    MeasurementValue(
                        measurementType = MeasurementType.LIGHT_INTENSITY,
                        value = 2560.0
                    ),
                    MeasurementValue(
                        measurementType = MeasurementType.SOIL_MOISTURE,
                        value = 12.0
                    ),
                )
            ),
            Measurement(
                id = 3,
                datetime = DateTimeFromApi(
                    originalString = "",
                    calendarInUTC0 = Calendar.getInstance()
                ),
                plantId = 1,
                deviceId = 1,
                values = listOf(
                    MeasurementValue(
                        measurementType = MeasurementType.TEMPERATURE,
                        value = 18.56
                    ),
                    MeasurementValue(
                        measurementType = MeasurementType.LIGHT_INTENSITY,
                        value = 520.0
                    ),
                    MeasurementValue(
                        measurementType = MeasurementType.SOIL_MOISTURE,
                        value = 68.0
                    ),
                )
            ),
        )
    }

    override suspend fun getMeasurementsOfPlant(plantId: Long, from: Calendar?, to: Calendar?): DataResult<List<Measurement>> {
        val matchingMeasurements = MEASUREMENTS.filter {
            it.plantId == plantId
        }
        return DataResult.Success(data = matchingMeasurements)
    }

    override suspend fun getLatestPlantMeasurementValues(plantId: Long): DataResult<List<LatestMeasurementValueOfPlant>> {
        val matchingMeasurement = MEASUREMENTS.filter {
            it.plantId == plantId
        }.maxByOrNull {
            it.id
        }
        val latestMeasurementValues = mutableListOf<LatestMeasurementValueOfPlant>()
        matchingMeasurement?.let { measurement ->
            measurement.values.forEach {
                val latestValue = LatestMeasurementValueOfPlant(
                    measurementType = it.measurementType,
                    value = it.value,
                    measurementId = measurement.id,
                    datetime = measurement.datetime
                )
                latestMeasurementValues.add(latestValue)
            }
        }
        return DataResult.Success(latestMeasurementValues)
    }


}