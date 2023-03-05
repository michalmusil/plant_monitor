package cz.mendelu.xmusil5.plantmonitor.ui.screens.devices_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.devices.IDevicesRepository
import cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.plants.IPlantsRepository
import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.api.device.GetDevice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DevicesViewModel @Inject constructor(
    private val devicesRepository: IDevicesRepository,
    private val plantsRepository: IPlantsRepository
): ViewModel() {

    val uiState: MutableState<DevicesUiState> = mutableStateOf(DevicesUiState.Start())

    fun fetchDevices(){
        viewModelScope.launch {
            val result = devicesRepository.getAllDevices()
            when(result){
                is CommunicationResult.Success -> {
                    if (result.data.isEmpty()){
                        uiState.value = DevicesUiState.NoDevicesYet()
                    }
                    else {
                        val devicesWithAddedPlants = addPlantsToDevices(
                            devicesWithoutPlants = result.data
                        )
                        uiState.value = DevicesUiState.DevicesLoaded(devicesWithAddedPlants)
                    }
                }
                is CommunicationResult.Error -> {
                    uiState.value = DevicesUiState.Error(R.string.somethingWentWrong)
                }
                is CommunicationResult.Exception -> {
                    uiState.value = DevicesUiState.Error(R.string.connectionError)
                }
            }
        }
    }

    private suspend fun addPlantsToDevices(devicesWithoutPlants: List<GetDevice>): List<GetDevice>{
        val devicesWithPlants = mutableListOf<GetDevice>()

        devicesWithoutPlants.forEach { device ->
            val deviceToAdd = device
            deviceToAdd.plantId?.let {
                val plantResult = plantsRepository.getPlantById(it)
                if (plantResult is CommunicationResult.Success){
                    deviceToAdd.plant = plantResult.data
                }
            }
            devicesWithPlants.add(deviceToAdd)
        }
        return devicesWithPlants
    }
}