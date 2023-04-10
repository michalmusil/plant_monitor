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
import cz.mendelu.xmusil5.plantmonitor.models.api.device.Device
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.Measurement
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.LatestMeasurementValueOfPlant
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementType
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.Plant
import cz.mendelu.xmusil5.plantmonitor.models.api.plant_note.PlantNote
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

    val from: MutableStateFlow<Calendar> = MutableStateFlow(
        DateUtils.getCalendarWithSubtractedElements(
            original = DateUtils.getCurrentCalendarInUTC0(),
            days = 10
        )
    )
    val to: MutableStateFlow<Calendar> = MutableStateFlow(
        DateUtils.getCurrentCalendarInUTC0()
    )

    val plant: MutableStateFlow<Plant?> = MutableStateFlow(null)
    val plantImageLoadedSucessfully: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val plantNotes: MutableStateFlow<List<PlantNote>?> = MutableStateFlow(null)
    val mostRecentMeasurementValues: MutableStateFlow<List<LatestMeasurementValueOfPlant>?> = MutableStateFlow(null)
    val measurements: MutableStateFlow<List<Measurement>?> = MutableStateFlow(null)
    val chartValueSets: MutableStateFlow<List<ChartValueSet>?> = MutableStateFlow(null)


    fun filterMeasurementsByDate(
        fromFilter: Calendar = this.from.value,
        toFilter: Calendar = this.to.value
    ){
        if (fromFilter.timeInMillis != from.value.timeInMillis){
            from.value = fromFilter
        }
        if (toFilter.timeInMillis != to.value.timeInMillis){
            to.value = toFilter
        }
        plant.value?.let {
            val fromInclusive = getInclusiveDate(
                originalCalendar = fromFilter,
                endInclusive = false
            )
            val toInclusive = getInclusiveDate(
                originalCalendar = toFilter,
                endInclusive = true
            )
            fetchPlantMeasurements(
                plantId = it.id,
                from = fromInclusive,
                to = toInclusive
            )
        }
    }

    fun fetchPlant(plantId: Long){
        viewModelScope.launch {
            val plantCall = plantsRepository.getPlantById(plantId)

            var resultPlant: Plant? = null
            var resultDevice: Device? = null

            when(plantCall){
                is CommunicationResult.Success -> {
                    resultPlant = plantCall.data
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
                fetchPlantMeasurements(
                    plantId = resultPlant.id,
                    from = from.value,
                    to = to.value
                )
                if (resultPlant.hasTitleImage){
                    fetchPlantImage(resultPlant.id)
                }
            }
        }
    }

    private fun fetchPlantImage(plantId: Long) {
        viewModelScope.launch {
            val result = plantsRepository.getPlantImage(plantId = plantId)
            if (result is CommunicationResult.Success){
                plant.value?.let {
                    plant.value = it.apply {
                        titleImageBitmap = result.data
                    }
                    plantImageLoadedSucessfully.value = true
                }
            }
        }
    }

    private suspend fun fetchPlantDevice(plantId: Long): Device?{
        val deviceCall = devicesRepository.getAllDevices()
        if (deviceCall is CommunicationResult.Success){
            return deviceCall.data.firstOrNull() {
                it.plantId == plantId
            }
        }
        return null
    }

    private fun fetchPlantMeasurements(plantId: Long, from: Calendar, to: Calendar){
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

    fun getAllChartValueSets(measurementsToFilter: List<Measurement>): List<ChartValueSet>{
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
        measurementsToFilter: List<Measurement>
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

    fun getMeasurementsOrderedForDisplay(measurements: List<Measurement>): List<Measurement>{
        return measurements.sortedByDescending {
            it.datetime?.calendarInUTC0?.timeInMillis ?: it.id
        }
    }

    fun getPlantNotesOrderedForDisplay(notes: List<PlantNote>): List<PlantNote>{
        return notes.sortedByDescending {
            it.created?.calendarInUTC0?.timeInMillis ?: it.id
        }
    }
}