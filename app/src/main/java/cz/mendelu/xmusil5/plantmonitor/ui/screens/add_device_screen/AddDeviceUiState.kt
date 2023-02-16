package cz.mendelu.xmusil5.plantmonitor.ui.screens.add_device_screen

sealed class AddDeviceUiState{
    class Start(): AddDeviceUiState()
    class Error(val errorStringCode: Int): AddDeviceUiState()
}
