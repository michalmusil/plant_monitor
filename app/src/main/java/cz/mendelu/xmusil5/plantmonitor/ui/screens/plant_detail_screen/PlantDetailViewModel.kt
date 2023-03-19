package cz.mendelu.xmusil5.plantmonitor.ui.screens.plant_detail_screen

import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madrapps.plot.line.DataPoint
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.devices.IDevicesRepository
import cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.measurements.IMeasurementsRepository
import cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.plants.IPlantsRepository
import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.api.device.GetDevice
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.GetMeasurement
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.LatestMeasurementValueOfPlant
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementType
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.GetPlant
import cz.mendelu.xmusil5.plantmonitor.models.charts.ChartValueSet
import cz.mendelu.xmusil5.plantmonitor.utils.DateUtils
import cz.mendelu.xmusil5.plantmonitor.utils.validation.measurements.IMeasurementsValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PlantDetailViewModel @Inject constructor(
    private val plantsRepository: IPlantsRepository,
    private val measurementsRepository: IMeasurementsRepository,
    private val devicesRepository: IDevicesRepository,
    val measurementsValidator: IMeasurementsValidator
): ViewModel() {

    val uiState: MutableState<PlantDetailUiState> = mutableStateOf(PlantDetailUiState.Start())

    fun fetchPlant(plantId: Long){
        viewModelScope.launch {
            val plantCall = plantsRepository.getPlantById(plantId)

            var resultPlant: GetPlant? = null
            var resultDevice: GetDevice? = null

            when(plantCall){
                is CommunicationResult.Success -> {
                    resultPlant = plantCall.data
                    if (resultPlant.hasTitleImage){
                        resultPlant.titleImageBitmap = fetchPlantImage(resultPlant)
                    }
                }
                is CommunicationResult.Error -> {
                    uiState.value = PlantDetailUiState.Error(R.string.somethingWentWrong)
                }
                is CommunicationResult.Exception -> {
                    uiState.value = PlantDetailUiState.Error(R.string.connectionError)
                }
            }

            if (resultPlant != null){
                resultDevice = fetchPlantDevice(resultPlant.id)
                resultDevice?.plant = resultPlant
                resultPlant.associatedDevice = resultDevice

                uiState.value = PlantDetailUiState.PlantLoaded(plant = resultPlant)
            }
        }
    }

    private suspend fun fetchPlantImage(plant: GetPlant): Bitmap?{
        val result = plantsRepository.getPlantImage(plantId = plant.id)
        if (result is CommunicationResult.Success){
            return result.data
        }
        return null
    }

    private suspend fun fetchPlantDevice(plantId: Long): GetDevice?{
        val deviceCall = devicesRepository.getAllDevices()
        if (deviceCall is CommunicationResult.Success){
            return deviceCall.data.firstOrNull() {
                it.plantId == plantId
            }
        }
        return null
    }

    fun fetchPlantMeasurements(plantId: Long, from: Calendar, to: Calendar){
        viewModelScope.launch {
            val result = measurementsRepository.getMeasurementsOfPlant(
                plantId = plantId,
                from = from,
                to = to
            )
            when(result){
                is CommunicationResult.Success -> {
                    uiState.value = PlantDetailUiState.MeasurementsLoaded(result.data)
                }
                is CommunicationResult.Error -> {
                    uiState.value = PlantDetailUiState.Error(R.string.somethingWentWrong)
                }
                is CommunicationResult.Exception -> {
                    uiState.value = PlantDetailUiState.Error(R.string.connectionError)
                }
            }
        }
    }

    fun fetchMostRecentValuesOfPlant(plant: GetPlant, onValuesFetched: (List<LatestMeasurementValueOfPlant>) -> Unit){
        viewModelScope.launch {
            val result = measurementsRepository.getLatestPlantMeasurementValues(
                plantId = plant.id,
            )
            if (result is CommunicationResult.Success){
                onValuesFetched(result.data)
            }
            else {
                onValuesFetched(listOf())
            }
        }
    }

    fun getChartValueSetOfType(
        measurementType: MeasurementType,
        measurementsToFilter: List<GetMeasurement>
    ): ChartValueSet{
        val dataPointsAndLabels: MutableList<Pair<DataPoint, String>> = mutableListOf()

        var indexOfValidValues = 0
        measurementsToFilter.forEach {measurement ->
            val measurementValue = measurement.getMeasurementValueByType(measurementType)

            // need both the value (y axis) and the date time taken (x axis) to add to chart set
            if (measurementValue != null && measurement.datetime != null){
                val dataPoint = DataPoint(
                    x = indexOfValidValues.toFloat(),
                    y = measurementValue.value.toFloat()
                )
                val label = DateUtils.getLocalizedDateTimeString(calendar = measurement.datetime.calendarInUTC0)

                dataPointsAndLabels.add(Pair(dataPoint, label))
                indexOfValidValues += 1
            }
        }

        val dataPoints = dataPointsAndLabels.map { it.first }
        val labels = dataPointsAndLabels.map { it.second }

        return ChartValueSet(
            measurementType = measurementType,
            set = dataPoints,
            labels = labels
        )
    }
}