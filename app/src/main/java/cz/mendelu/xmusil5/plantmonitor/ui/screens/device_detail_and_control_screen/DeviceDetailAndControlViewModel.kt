package cz.mendelu.xmusil5.plantmonitor.ui.screens.device_detail_and_control_screen

import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.devices.IDevicesRepository
import cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.plants.IPlantsRepository
import cz.mendelu.xmusil5.plantmonitor.communication.utils.DataResult
import cz.mendelu.xmusil5.plantmonitor.models.api.device.PutDeviceActivation
import cz.mendelu.xmusil5.plantmonitor.models.api.device.PutDevicePlantAssignment
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.Plant
import cz.mendelu.xmusil5.plantmonitor.utils.image.ImageQuality
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
                is DataResult.Success -> {
                    val device = result.data
                    device.plantId?.let {
                        device.plant = fetchDevicePlant(it)
                    }
                    uiState.value = DeviceDetailAndControlUiState.DeviceLoaded(device)
                    isLoading.value = false
                }
                is DataResult.Error -> {
                    uiState.value = DeviceDetailAndControlUiState.Error(R.string.somethingWentWrong)
                    isLoading.value = false
                }
                is DataResult.Exception -> {
                    uiState.value = DeviceDetailAndControlUiState.Error(R.string.connectionError)
                    isLoading.value = false
                }
            }
        }
    }

    private suspend fun fetchDevicePlant(plantId: Long): Plant?{
        val result = plantsRepository.getPlantById(plantId)
        if (result is DataResult.Success){
            return result.data
        }
        return null
    }

    fun fetchPlants(onSuccess: (List<Plant>) -> Unit){
        viewModelScope.launch {
            isLoading.value = true
            val result = plantsRepository.getAllPlants()
            if (result is DataResult.Success){
                onSuccess(result.data)
            }
            isLoading.value = false
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

    fun proceedDeviceActivation(active: Boolean, deviceId: Long){
        viewModelScope.launch {
            isLoading.value = true
            val deviceActivationPost = PutDeviceActivation(
                deviceId = deviceId,
                isActive = active
            )
            val result = devicesRepository.deviceActivation(deviceActivationPost)
            when (result) {
                is DataResult.Success -> {
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
                is DataResult.Success -> {
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

    fun unregisterDevice(deviceId: Long){
        viewModelScope.launch {
            val result = devicesRepository.unregisterDevice(deviceId = deviceId)
            when(result){
                is DataResult.Success -> {
                    uiState.value = DeviceDetailAndControlUiState.DeviceUnregistered()
                }
                is DataResult.Error -> {
                    uiState.value = DeviceDetailAndControlUiState.DeviceUpdateFailed(errorMessageCode = R.string.somethingWentWrong)
                }
                is DataResult.Exception -> {
                    uiState.value = DeviceDetailAndControlUiState.Error(errorStringCode = R.string.connectionError)
                }
            }
        }
    }
}