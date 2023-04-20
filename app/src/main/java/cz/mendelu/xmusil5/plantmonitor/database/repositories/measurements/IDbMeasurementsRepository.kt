package cz.mendelu.xmusil5.plantmonitor.database.repositories.measurements

import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.database.entities.DbMeasurement
import cz.mendelu.xmusil5.plantmonitor.models.database.helpers.DbLatestMeasurementPlantValue
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

interface IDbMeasurementsRepository {
    suspend fun getByPlantIdInTimeRange(plantId: Long, from: Calendar, to: Calendar): CommunicationResult<List<DbMeasurement>>

    suspend fun getLatestValuesOfPlant(plantId: Long): CommunicationResult<List<DbLatestMeasurementPlantValue>>

    suspend fun getLatestPlantMeasurement(plantId: Long): CommunicationResult<DbMeasurement>

    suspend fun addMeasurement(measurement: DbMeasurement): CommunicationResult<DbMeasurement>
}