package cz.mendelu.xmusil5.plantmonitor.ui.screens.devices_screen

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
class DevicesViewModel @Inject constructor(
    private val devicesRepository: IDevicesRepository
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
                        uiState.value = DevicesUiState.DevicesLoaded(result.data)
                    }
                }
                is CommunicationResult.Error -> {
                    uiState.value = DevicesUiState.Error(R.string.connectionError)
                }
                is CommunicationResult.Exception -> {
                    uiState.value = DevicesUiState.Error(R.string.connectionError)
                }
            }
        }
    }
}