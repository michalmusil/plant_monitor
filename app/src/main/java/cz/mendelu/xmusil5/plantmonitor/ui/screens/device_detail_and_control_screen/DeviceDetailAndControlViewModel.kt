package cz.mendelu.xmusil5.plantmonitor.ui.screens.device_detail_and_control_screen

import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.communication.repositories.devices.IDevicesRepository
import cz.mendelu.xmusil5.plantmonitor.communication.repositories.plants.IPlantsRepository
import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.api.device.PutDeviceActivation
import cz.mendelu.xmusil5.plantmonitor.models.api.device.PutDevicePlantAssignment
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.GetPlant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeviceDetailAndControlViewModel @Inject constructor(
    private val devicesRepository: IDevicesRepository,
    private val plantsRepository: IPlantsRepository
): ViewModel() {

    val uiState: MutableState<DeviceDetailAndControlUiState> = mutableStateOf(DeviceDetailAndControlUiState.Start())
    val isLoading: MutableState<Boolean> = mutableStateOf(false)

    fun fetchDevice(deviceId: Long){
        viewModelScope.launch {
            isLoading.value = true
            val result = devicesRepository.getDeviceById(deviceId)
            when(result){
                is CommunicationResult.Success -> {
                    val device = result.data
                    device.plantId?.let {
                        device.plant = fetchDevicePlant(it)
                    }
                    uiState.value = DeviceDetailAndControlUiState.DeviceLoaded(device)
                    isLoading.value = false
                }
                is CommunicationResult.Error -> {
                    uiState.value = DeviceDetailAndControlUiState.Error(R.string.somethingWentWrong)
                    isLoading.value = false
                }
                is CommunicationResult.Exception -> {
                    uiState.value = DeviceDetailAndControlUiState.Error(R.string.connectionError)
                    isLoading.value = false
                }
            }
        }
    }

    private suspend fun fetchDevicePlant(plantId: Long): GetPlant?{
        val result = plantsRepository.getPlantById(plantId)
        if (result is CommunicationResult.Success){
            return result.data
        }
        return null
    }

    fun fetchPlants(onSuccess: (List<GetPlant>) -> Unit){
        viewModelScope.launch {
            isLoading.value = true
            val result = plantsRepository.getAllPlants()
            if (result is CommunicationResult.Success){
                onSuccess(result.data)
            }
            isLoading.value = false
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

    fun proceedDeviceActivation(active: Boolean, deviceId: Long){
        viewModelScope.launch {
            isLoading.value = true
            val deviceActivationPost = PutDeviceActivation(
                deviceId = deviceId,
                isActive = active
            )
            val result = devicesRepository.deviceActivation(deviceActivationPost)
            when (result) {
                is CommunicationResult.Success -> {
                    val device = result.data
                    device.plantId?.let {
                        device.plant = fetchDevicePlant(it)
                    }
                    uiState.value = DeviceDetailAndControlUiState.DeviceUpdated(device)
                    isLoading.value = false
                }
                else -> {
                    uiState.value = DeviceDetailAndControlUiState.DeviceUpdateFailed(R.string.deviceUpdateFailed)
                    isLoading.value = false
                }
            }
        }
    }

    fun assignDeviceToPlant(deviceId: Long, plantId: Long){
        viewModelScope.launch {
            isLoading.value = true
            val deviceAssignmentPost = PutDevicePlantAssignment(
                deviceId = deviceId,
                plantId = plantId
            )
            val result = devicesRepository.assignDeviceToPlant(deviceAssignmentPost)
            when (result) {
                is CommunicationResult.Success -> {
                    val device = result.data
                    device.plantId?.let {
                        device.plant = fetchDevicePlant(it)
                    }
                    uiState.value = DeviceDetailAndControlUiState.DeviceUpdated(device)
                    isLoading.value = false
                }
                else -> {
                    isLoading.value = false
                    uiState.value = DeviceDetailAndControlUiState.DeviceUpdateFailed(R.string.deviceUpdateFailed)
                }
            }
        }
    }
}