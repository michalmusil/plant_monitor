package cz.mendelu.xmusil5.plantmonitor.database.repositories.measurements

import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.database.daos.MeasurementsDao
import cz.mendelu.xmusil5.plantmonitor.database.repositories.BaseDbRepository
import cz.mendelu.xmusil5.plantmonitor.models.database.entities.DbMeasurement
import cz.mendelu.xmusil5.plantmonitor.models.database.helpers.DbLatestMeasurementPlantValue
import cz.mendelu.xmusil5.plantmonitor.models.enums.MeasurementType
import cz.mendelu.xmusil5.plantmonitor.utils.DateUtils
import java.util.*

class DbMeasurementsRepositoryImpl(
    private val measurementsDao: MeasurementsDao
): BaseDbRepository(), IDbMeasurementsRepository {

    override suspend fun getByPlantIdInTimeRange(
        plantId: Long,
        from: Calendar,
        to: Calendar
    ): CommunicationResult<List<DbMeasurement>> {
        return processDbCall {
            measurementsDao.getByPlantIdInTimeRange(
                plantId = plantId,
                fromMillis = from.timeInMillis,
                toMillis = to.timeInMillis
            )
        }
    }

    override suspend fun getLatestValuesOfPlant(plantId: Long): CommunicationResult<List<DbLatestMeasurementPlantValue>> {
        val allMeasurements = processDbCall {
            measurementsDao.getByPlantIdInTimeRange(
                plantId = plantId,
                fromMillis = 0L,
                toMillis = DateUtils.getCurrentCalendarInUTC0().timeInMillis
            )
        }
        when(allMeasurements){
            is CommunicationResult.Error -> {
                return CommunicationResult.Error(error = allMeasurements.error)
            }
            is CommunicationResult.Exception -> {
                return CommunicationResult.Exception(exception = allMeasurements.exception)
            }

            is CommunicationResult.Success -> {
                val measurementTypes = MeasurementType.getValidTypes()
                val latestValues = mutableListOf<DbLatestMeasurementPlantValue>()
                measurementTypes.forEach { type ->
                    val latestMeasurementWithType = allMeasurements.data.filter { measurement ->
                        val measurementValueOfType = measurement.values.firstOrNull{
                            it.measurementType == type
                        }
                        measurementValueOfType != null && measurement.datetime != null
                    }.maxByOrNull {
                        it.datetime?.timeInMillis ?: 0
                    }
                    val valueOfLatestMeasurement = latestMeasurementWithType?.values?.firstOrNull {
                        it.measurementType == type
                    }

                    if (latestMeasurementWithType != null && valueOfLatestMeasurement != null) {
                        val newLatestValue = DbLatestMeasurementPlantValue(
                            measurementType = valueOfLatestMeasurement.measurementType,
                            value = valueOfLatestMeasurement.value,
                            measurementId = latestMeasurementWithType.id,
                            datetime = latestMeasurementWithType.datetime!!
                        )
                        latestValues.add(newLatestValue)
                    }
                }

                return CommunicationResult.Success(latestValues)

            }
        }
    }

    override suspend fun getLatestPlantMeasurement(plantId: Long): CommunicationResult<DbMeasurement> {
        return processDbCall {
            measurementsDao.getLatestByPlantId(plantId)
        }
    }

    override suspend fun addMeasurement(measurement: DbMeasurement): CommunicationResult<DbMeasurement> {
        val result = processDbCall {
            measurementsDao.insert(measurement)
        }
        return when (result){
            is CommunicationResult.Success -> {
                CommunicationResult.Success(measurement)
            }
            is CommunicationResult.Error -> {
                CommunicationResult.Error(error = result.error)
            }
            is CommunicationResult.Exception -> {
                CommunicationResult.Exception(exception = result.exception)
            }
        }
    }
}