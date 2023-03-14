package cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.measurements

import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationError
import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.GetMeasurement
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementType
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementValue
import cz.mendelu.xmusil5.plantmonitor.models.api.utils.DateTimeFromApi
import cz.mendelu.xmusil5.plantmonitor.utils.DateUtils
import java.util.*

class MeasurementsRepositoryMock: IMeasurementsRepository {

    companion object{
        val MEASUREMENTS = listOf(
            GetMeasurement(
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
            GetMeasurement(
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
            GetMeasurement(
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

    override suspend fun getMeasurementsOfPlant(plantId: Long, from: Calendar?, to: Calendar?): CommunicationResult<List<GetMeasurement>> {
        val matchingMeasurements = MEASUREMENTS.filter {
            it.plantId == plantId
        }
        return CommunicationResult.Success(data = matchingMeasurements)
    }

    override suspend fun getLatestPlantMeasurementOfType(
        plantId: Long,
        measurementType: MeasurementType
    ): CommunicationResult<GetMeasurement> {
        val found = MEASUREMENTS.filter {
            it.plantId == plantId
        }.sortedByDescending {
            it.datetime?.calendarInUTC0?.timeInMillis
        }.firstOrNull()

        found?.let {
            return CommunicationResult.Success(data = it)
        }
        return CommunicationResult.Error(
            error = CommunicationError(
                code = 404,
                message = "Measurement not found"
            )
        )
    }

    override suspend fun getMostRecentPlantMeasurementValues(plantId: Long): CommunicationResult<List<MeasurementValue>> {
        val mostRecentMeasurement = MEASUREMENTS.filter {
            it.plantId == plantId
        }.sortedByDescending {
            it.datetime?.calendarInUTC0?.timeInMillis
        }.firstOrNull()

        mostRecentMeasurement?.let {
            return CommunicationResult.Success(data = it.values)
        }
        return CommunicationResult.Error(
            error = CommunicationError(
                code = 404,
                message = "Measurement not found"
            )
        )
    }


}