package cz.mendelu.xmusil5.plantmonitor.ui.screens.plants_screen

import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.measurements.IMeasurementsRepository
import cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.plants.IPlantsRepository
import cz.mendelu.xmusil5.plantmonitor.communication.utils.DataResult
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.LatestMeasurementValueOfPlant
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.Plant
import cz.mendelu.xmusil5.plantmonitor.utils.image.ImageQuality
import cz.mendelu.xmusil5.plantmonitor.utils.validation.measurements.IMeasurementsValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlantsViewModel @Inject constructor(
    private val plantsRepository: IPlantsRepository,
    private val measurementsRepository: IMeasurementsRepository,
    val measurementsValidator: IMeasurementsValidator
): ViewModel() {

    val uiState: MutableState<PlantsUiState> = mutableStateOf(PlantsUiState.Start())

    fun fetchPlants(){
        viewModelScope.launch {
            val result = plantsRepository.getAllPlants()
            result.let {
                when(it){
                    is DataResult.Success -> {
                        if(it.data.isEmpty()) {
                            uiState.value = PlantsUiState.NoPlantsYet()
                        }
                        else{
                            uiState.value = PlantsUiState.PlantsLoaded(plants = it.data)
                        }
                    }
                    is DataResult.Exception -> {
                        uiState.value = PlantsUiState.Error(R.string.connectionError)
                    }
                    is DataResult.Error -> {
                        uiState.value = PlantsUiState.Error(R.string.somethingWentWrong)
                    }
                }
            }
        }
    }

    fun fetchMostRecentValuesOfPlant(plant: Plant, onValuesFetched: (List<LatestMeasurementValueOfPlant>) -> Unit){
        viewModelScope.launch {
            val result = measurementsRepository.getLatestPlantMeasurementValues(
                plantId = plant.id,
            )
            if (result is DataResult.Success){
                onValuesFetched(result.data)
            }
            else {
                onValuesFetched(listOf())
            }
        }
    }

    fun fetchPlantImage(
        plant: Plant,
        onSuccess: (Bitmap) -> Unit,
        onFailure: () -> Unit = {}
    ){
        if (!plant.hasTitleImage){
            onFailure()
            return
        }
        viewModelScope.launch {
            val response = plantsRepository.getPlantImage(
                plantId = plant.id,
                imageQuality = ImageQuality.SMALL
            )
            when(response){
                is DataResult.Success -> {
                    onSuccess(response.data)
                }
                else -> {
                    onFailure()
                }
            }
        }
    }
}