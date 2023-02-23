package cz.mendelu.xmusil5.plantmonitor.ui.screens.plant_detail_screen

import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.communication.repositories.measurements.IMeasurementsRepository
import cz.mendelu.xmusil5.plantmonitor.communication.repositories.plants.IPlantsRepository
import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.GetMeasurement
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementType
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementValue
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.GetPlant
import cz.mendelu.xmusil5.plantmonitor.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PlantDetailViewModel @Inject constructor(
    private val plantsRepository: IPlantsRepository,
    private val measurementsRepository: IMeasurementsRepository
): ViewModel() {

    val uiState: MutableState<PlantDetailUiState> = mutableStateOf(PlantDetailUiState.Start())

    fun fetchPlant(plantId: Long){
        viewModelScope.launch {
            val result = plantsRepository.getPlantById(plantId)
            when(result){
                is CommunicationResult.Success -> {
                    val resultPlant = result.data
                    if (resultPlant.hasTitleImage){
                        resultPlant.titleImageBitmap = fetchPlantImage(resultPlant)
                    }
                    uiState.value = PlantDetailUiState.PlantLoaded(resultPlant)
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

    private suspend fun fetchPlantImage(plant: GetPlant): Bitmap?{
        val result = plantsRepository.getPlantImage(plantId = plant.id)
        if (result is CommunicationResult.Success){
            return result.data
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

    fun fetchMostRecentValuesOfPlant(plant: GetPlant, onValuesFetched: (List<MeasurementValue>) -> Unit){
        viewModelScope.launch {
            val result = measurementsRepository.getMostRecentPlantMeasurementValues(
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
}