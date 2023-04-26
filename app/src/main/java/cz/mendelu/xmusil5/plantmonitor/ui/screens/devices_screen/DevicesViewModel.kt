package cz.mendelu.xmusil5.plantmonitor.ui.screens.devices_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.devices.IDevicesRepository
import cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.plants.IPlantsRepository
import cz.mendelu.xmusil5.plantmonitor.communication.utils.DataResult
import cz.mendelu.xmusil5.plantmonitor.models.api.device.Device
import cz.mendelu.xmusil5.plantmonitor.models.api.device.PutDeviceRegister
import cz.mendelu.xmusil5.plantmonitor.utils.validation.strings.IStringValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DevicesViewModel @Inject constructor(
    private val devicesRepository: IDevicesRepository,
    private val plantsRepository: IPlantsRepository,
    val stringValidator: IStringValidator
): ViewModel() {

    val uiState: MutableState<DevicesUiState> = mutableStateOf(DevicesUiState.Start())

    fun fetchDevices(){
        viewModelScope.launch {
            val result = devicesRepository.getAllDevices()
            when(result){
                is DataResult.Success -> {
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
                is DataResult.Error -> {
                    uiState.value = DevicesUiState.Error(R.string.somethingWentWrong)
                }
                is DataResult.Exception -> {
                    uiState.value = DevicesUiState.Error(R.string.connectionError)
                }
            }
        }
    }

    private suspend fun addPlantsToDevices(devicesWithoutPlants: List<Device>): List<Device>{
        val devicesWithPlants = mutableListOf<Device>()

        devicesWithoutPlants.forEach { device ->
            val deviceToAdd = device
            deviceToAdd.plantId?.let {
                val plantResult = plantsRepository.getPlantById(it)
                if (plantResult is DataResult.Success){
                    deviceToAdd.plant = plantResult.data
                }
            }
            devicesWithPlants.add(deviceToAdd)
        }
        return devicesWithPlants
    }

    fun addNewDevice(
        communicationIdentifier: String,
        macAddress: String,
        onError: () -> Unit
    ){
        viewModelScope.launch {
            val deviceRegistration = PutDeviceRegister(
                communicationId = communicationIdentifier,
                macAddress = macAddress
            )
            val result = devicesRepository.registerDevice(deviceRegister = deviceRegistration)
            when(result){
                is DataResult.Success -> {
                    fetchDevices()
                }
                is DataResult.Error -> {
                    onError()
                }
                is DataResult.Exception -> {
                    uiState.value = DevicesUiState.Error(errorStringCode = R.string.connectionError)
                }
            }
        }
    }
}