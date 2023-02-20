package cz.mendelu.xmusil5.plantmonitor.ui.screens.plants_screen

import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.communication.repositories.plants.IPlantsRepository
import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.GetPlant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlantsViewModel @Inject constructor(
    private val plantsRepository: IPlantsRepository
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