package cz.mendelu.xmusil5.plantmonitor.ui.screens.device_detail_and_control_screen

import cz.mendelu.xmusil5.plantmonitor.models.api.device.Device

sealed class DeviceDetailAndControlUiState{
    class Start(): DeviceDetailAndControlUiState()
    class DeviceLoaded(val device: Device): DeviceDetailAndControlUiState()
    class DeviceUpdated(val device: Device): DeviceDetailAndControlUiState()
    class DeviceUpdateFailed(val errorMessageCode: Int): DeviceDetailAndControlUiState()
    class DeviceUnregistered(): DeviceDetailAndControlUiState()
    class Error(val errorStringCode: Int): DeviceDetailAndControlUiState()
}
