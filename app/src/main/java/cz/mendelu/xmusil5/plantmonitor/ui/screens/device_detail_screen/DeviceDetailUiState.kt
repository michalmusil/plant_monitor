package cz.mendelu.xmusil5.plantmonitor.ui.screens.device_detail_screen

sealed class DeviceDetailUiState{
    class Start(): DeviceDetailUiState()
    class Error(val errorStringCode: Int): DeviceDetailUiState()
}
