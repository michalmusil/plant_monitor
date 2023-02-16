package cz.mendelu.xmusil5.plantmonitor.ui.screens.devices_screen

sealed class DevicesUiState{
    class Start(): DevicesUiState()
    class Error(val errorStringCode: Int): DevicesUiState()
}