package cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.measurements

import cz.mendelu.xmusil5.plantmonitor.authentication.IAuthenticationManager
import cz.mendelu.xmusil5.plantmonitor.communication.api.HousePlantMeasurementsApi
import cz.mendelu.xmusil5.plantmonitor.communication.utils.BaseApiRepository
import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.GetMeasurement
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementType
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementValue
import cz.mendelu.xmusil5.plantmonitor.utils.DateUtils
import kotlinx.coroutines.async
import java.util.*
import javax.inject.Inject

class MeasurementsRepositoryImpl @Inject constructor(
    authenticationManager: IAuthenticationManager,
    private val api: HousePlantMeasurementsApi
): BaseApiRepository(authenticationManager), IMeasurementsRepository {

    override suspend fun getMeasurementsOfPlant(plantId: Long, from: Calendar?, to: Calendar?): CommunicationResult<List<GetMeasurement>> {
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
                bearerToken = authenticationManager.getToken()
            )
        }
    }

    override suspend fun getLatestPlantMeasurementOfType(
        plantId: Long,
        measurementType: MeasurementType
    ): CommunicationResult<GetMeasurement> {
        return processRequest {
            api.getLatestPlantMeasurementOfType(
                plantId = plantId,
                measurementTypeNumber = measurementType.typeNumber,
                bearerToken = authenticationManager.getToken()
            )
        }
    }

    override suspend fun getMostRecentPlantMeasurementValues(plantId: Long): CommunicationResult<List<MeasurementValue>> {
        val temperature = getLatestPlantMeasurementOfType(
            plantId = plantId,
            measurementType = MeasurementType.TEMPERATURE
        )
        val lightIntensity = getLatestPlantMeasurementOfType(
            plantId = plantId,
            measurementType = MeasurementType.LIGHT_INTENSITY
        )
        val soilMoisture = getLatestPlantMeasurementOfType(
            plantId = plantId,
            measurementType = MeasurementType.SOIL_MOISTURE
        )

        val measurementResults = mutableListOf<Pair<MeasurementType, CommunicationResult<GetMeasurement>>>()
        measurementResults.add(Pair(MeasurementType.TEMPERATURE, temperature))
        measurementResults.add(Pair(MeasurementType.LIGHT_INTENSITY, lightIntensity))
        measurementResults.add(Pair(MeasurementType.SOIL_MOISTURE, soilMoisture))

        val measurementValues = mutableListOf<MeasurementValue>()

        measurementResults.forEach{ result ->
            result.second.let { communicationResult ->
                if (communicationResult is CommunicationResult.Success){
                    communicationResult.data.getMeasurementValueByType(
                        measurementType = result.first
                    )?.let {
                        val foundValue = it
                        foundValue.measurementDate = communicationResult.data.datetime
                        measurementValues.add(it)
                    }
                }
            }
        }

        return CommunicationResult.Success(
            data = measurementValues
        )
    }


}