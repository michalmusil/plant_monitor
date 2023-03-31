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
import cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.plant_notes.IPlantNotesRepository
import cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.plants.IPlantsRepository
import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.api.device.GetDevice
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.GetMeasurement
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.LatestMeasurementValueOfPlant
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementType
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.GetPlant
import cz.mendelu.xmusil5.plantmonitor.models.api.plant_note.GetPlantNote
import cz.mendelu.xmusil5.plantmonitor.models.api.plant_note.PostPlantNote
import cz.mendelu.xmusil5.plantmonitor.models.charts.ChartValueSet
import cz.mendelu.xmusil5.plantmonitor.utils.DateUtils
import cz.mendelu.xmusil5.plantmonitor.utils.validation.measurements.IMeasurementsValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PlantDetailViewModel @Inject constructor(
    private val plantsRepository: IPlantsRepository,
    private val measurementsRepository: IMeasurementsRepository,
    private val devicesRepository: IDevicesRepository,
    private val plantNotesRepository: IPlantNotesRepository,
    val measurementsValidator: IMeasurementsValidator
): ViewModel() {

    val uiState: MutableState<PlantDetailUiState> = mutableStateOf(PlantDetailUiState.Start())

    val plant: MutableStateFlow<GetPlant?> = MutableStateFlow(null)
    val plantNotes: MutableStateFlow<List<GetPlantNote>?> = MutableStateFlow(null)
    val mostRecentMeasurementValues: MutableStateFlow<List<LatestMeasurementValueOfPlant>?> = MutableStateFlow(null)
    val measurements: MutableStateFlow<List<GetMeasurement>?> = MutableStateFlow(null)
    val chartValueSets: MutableStateFlow<List<ChartValueSet>?> = MutableStateFlow(null)


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

                plant.value = resultPlant
                fetchPlantNotes(resultPlant.id)

                uiState.value = PlantDetailUiState.PlantLoaded()
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
                    measurements.value = result.data
                    fetchMostRecentValuesOfPlant(plantId)
                    chartValueSets.value = getAllChartValueSets(result.data)

                    uiState.value = PlantDetailUiState.MeasurementsLoaded()
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

    fun fetchMostRecentValuesOfPlant(plantId: Long){
        viewModelScope.launch {
            val result = measurementsRepository.getLatestPlantMeasurementValues(
                plantId = plantId
            )
            if (result is CommunicationResult.Success){
                mostRecentMeasurementValues.value = result.data
            }
        }
    }

    fun fetchPlantNotes(plantId: Long){
        viewModelScope.launch {
            val result = plantNotesRepository.getByPlantId(plantId = plantId)

            if (result is CommunicationResult.Success){
                plantNotes.value = result.data
            }
        }
    }

    fun addPlantNote(text: String, plantId: Long){
        if (text.isNotBlank()){
            val newNote = PostPlantNote(
                text = text,
                plantId = plantId
            )
            viewModelScope.launch {
                val result = plantNotesRepository.addNewPlantNote(newNote)
                when(result){
                    is CommunicationResult.Success -> {
                        plant.value?.let {
                            fetchPlantNotes(it.id)
                        }
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
    }

    fun deletePlantNote(noteId: Long){
        viewModelScope.launch {
            val result = plantNotesRepository.deletePlantNote(noteId)
            when(result){
                is CommunicationResult.Success -> {
                    plant.value?.let {
                        fetchPlantNotes(it.id)
                    }
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




    fun getInclusiveDate(
        originalCalendar: Calendar,
        endInclusive: Boolean
    ): Calendar{
        val inclusiveDate = DateUtils.getCalendarFromDateComponents(
            year = originalCalendar.get(Calendar.YEAR),
            month = originalCalendar.get(Calendar.MONTH),
            day = originalCalendar.get(Calendar.DAY_OF_MONTH)
        )
        if (endInclusive) {
            inclusiveDate.apply {
                set(Calendar.DAY_OF_MONTH, get(Calendar.DAY_OF_MONTH))
                set(Calendar.HOUR, 23)
                set(Calendar.MINUTE, 59)
                set(Calendar.SECOND, 59)
                set(Calendar.MILLISECOND, 999)
            }
        } else {
            inclusiveDate.apply {
                set(Calendar.DAY_OF_MONTH, get(Calendar.DAY_OF_MONTH))
                set(Calendar.HOUR, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
        }
        return inclusiveDate
    }

    fun getAllChartValueSets(measurementsToFilter: List<GetMeasurement>): List<ChartValueSet>{
        val chartValueSets = mutableListOf<ChartValueSet>()
        MeasurementType.getValidTypes().forEach {
            val set = getChartValueSetOfType(
                measurementType = it,
                measurementsToFilter = measurementsToFilter
            )
            chartValueSets.add(set)
        }
        return chartValueSets
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

    fun getMeasurementsOrderedForDisplay(measurements: List<GetMeasurement>): List<GetMeasurement>{
        return measurements.sortedByDescending {
            it.datetime?.calendarInUTC0?.timeInMillis ?: it.id
        }
    }

    fun getPlantNotesOrderedForDisplay(notes: List<GetPlantNote>): List<GetPlantNote>{
        return notes.sortedByDescending {
            it.created?.calendarInUTC0?.timeInMillis ?: it.id
        }
    }
}