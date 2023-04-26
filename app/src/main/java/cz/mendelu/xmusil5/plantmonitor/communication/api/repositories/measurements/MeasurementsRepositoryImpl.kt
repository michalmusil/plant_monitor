package cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.measurements

import cz.mendelu.xmusil5.plantmonitor.user_session.IUserSessionManager
import cz.mendelu.xmusil5.plantmonitor.communication.api.HousePlantMeasurementsApi
import cz.mendelu.xmusil5.plantmonitor.communication.utils.BaseApiRepository
import cz.mendelu.xmusil5.plantmonitor.communication.utils.DataResult
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.Measurement
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.LatestMeasurementValueOfPlant
import cz.mendelu.xmusil5.plantmonitor.utils.DateUtils
import java.util.*
import javax.inject.Inject

class MeasurementsRepositoryImpl @Inject constructor(
    userSessionManager: IUserSessionManager,
    private val api: HousePlantMeasurementsApi
): BaseApiRepository(userSessionManager), IMeasurementsRepository {

    override suspend fun getMeasurementsOfPlant(plantId: Long, from: Calendar?, to: Calendar?): DataResult<List<Measurement>> {
        val stringFrom = when(from){
            is Calendar -> DateUtils.apiDateStringFromCalendar(from)
            else -> {null}
        }
        val stringTo = when(to){
            is Calendar -> DateUtils.apiDateStringFromCalendar(to)
            else -> {null}
        }

        return processRequest{
            api.getMeasurementsOfPlant(
                plantId = plantId,
                from = stringFrom,
                to = stringTo,
                bearerToken = userSessionManager.getToken()
            )
        }
    }

    override suspend fun getLatestPlantMeasurementValues(plantId: Long): DataResult<List<LatestMeasurementValueOfPlant>> {
        return processRequest {
            api.getLatestPlantMeasurementValues(
                plantId = plantId,
                bearerToken = userSessionManager.getToken()
            )
        }
    }


}