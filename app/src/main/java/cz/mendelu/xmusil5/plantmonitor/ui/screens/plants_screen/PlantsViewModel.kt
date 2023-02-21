package cz.mendelu.xmusil5.plantmonitor.ui.screens.plants_screen

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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlantsViewModel @Inject constructor(
    private val plantsRepository: IPlantsRepository,
    private val measurementsRepository: IMeasurementsRepository
): ViewModel() {

    val uiState: MutableState<PlantsUiState> = mutableStateOf(PlantsUiState.Start())

    fun fetchPlants(){
        viewModelScope.launch {
            val result = plantsRepository.getAllPlants()
            result.let {
                when(it){
                    is CommunicationResult.Success -> {
                        uiState.value = PlantsUiState.PlantsLoaded(plants = it.data)
                    }
                    is CommunicationResult.Exception -> {
                        uiState.value = PlantsUiState.Error(R.string.somethingWentWrong)
                    }
                    is CommunicationResult.Error -> {
                        uiState.value = PlantsUiState.Error(R.string.somethingWentWrong)
                    }
                }
            }
        }
    }

    fun fetchMostRecentValuesOfPlant(plant: GetPlant, onValuesFetched: (List<MeasurementValue>) -> Unit){
        viewModelScope.launch {
            val temperature = async { measurementsRepository.getLatestPlantMeasurementOfType(
                plantId = plant.id,
                measurementType = MeasurementType.TEMPERATURE
            ) }
            val lightIntensity = async { measurementsRepository.getLatestPlantMeasurementOfType(
                plantId = plant.id,
                measurementType = MeasurementType.LIGHT_INTENSITY
            ) }
            val soilMoisture = async { measurementsRepository.getLatestPlantMeasurementOfType(
                plantId = plant.id,
                measurementType = MeasurementType.SOIL_MOISTURE
            ) }

            val measurementResults = mutableListOf<Pair<MeasurementType, CommunicationResult<GetMeasurement>>>()
            measurementResults.add(Pair(MeasurementType.TEMPERATURE, temperature.await()))
            measurementResults.add(Pair(MeasurementType.LIGHT_INTENSITY, lightIntensity.await()))
            measurementResults.add(Pair(MeasurementType.SOIL_MOISTURE, soilMoisture.await()))

            val measurementValues = mutableListOf<MeasurementValue>()

            measurementResults.forEach{ result ->
                result.second.let {
                    if (it is CommunicationResult.Success){
                        it.data.getMeasurementValueByType(
                            measurementType = result.first
                        )?.let {
                            measurementValues.add(it)
                        }
                    }
                }
            }

            onValuesFetched(measurementValues)
        }
    }

    fun fetchPlantImage(
        plant: GetPlant,
        onSuccess: (Bitmap) -> Unit,
        onFailure: () -> Unit = {}
    ){
        if (!plant.hasTitleImage){
            onFailure()
            return
        }
        viewModelScope.launch {
            val response = plantsRepository.getPlantImage(plantId = plant.id)
            when(response){
                is CommunicationResult.Success -> {
                    onSuccess(response.data)
                }
                else -> {
                    onFailure()
                }
            }
        }
    }
}