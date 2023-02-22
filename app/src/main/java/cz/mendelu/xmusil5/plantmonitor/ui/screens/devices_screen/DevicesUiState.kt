package cz.mendelu.xmusil5.plantmonitor.ui.screens.devices_screen

import cz.mendelu.xmusil5.plantmonitor.models.api.device.GetDevice

sealed class DevicesUiState{
    class Start(): DevicesUiState()
    class NoDevicesYet(): DevicesUiState()
    class DevicesLoaded(val devices: List<GetDevice>): DevicesUiState()
    class Error(val errorStringCode: Int): DevicesUiState()
}