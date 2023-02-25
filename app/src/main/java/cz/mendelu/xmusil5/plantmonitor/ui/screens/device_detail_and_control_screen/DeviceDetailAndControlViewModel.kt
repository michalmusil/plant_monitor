package cz.mendelu.xmusil5.plantmonitor.ui.screens.device_detail_and_control_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.communication.repositories.devices.IDevicesRepository
import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeviceDetailAndControlViewModel @Inject constructor(
    private val devicesRepository: IDevicesRepository
): ViewModel() {

    val uiState: MutableState<DeviceDetailAndControlUiState> = mutableStateOf(DeviceDetailAndControlUiState.Start())

    fun fetchDevice(deviceId: Long){
        viewModelScope.launch {
            val result = devicesRepository.getDeviceById(deviceId)
            when(result){
                is CommunicationResult.Success -> {
                    uiState.value = DeviceDetailAndControlUiState.DeviceLoaded(result.data)
                }
                is CommunicationResult.Error -> {
                    uiState.value = DeviceDetailAndControlUiState.Error(R.string.somethingWentWrong)
                }
                is CommunicationResult.Exception -> {
                    uiState.value = DeviceDetailAndControlUiState.Error(R.string.connectionError)
                }
            }
        }
    }
}