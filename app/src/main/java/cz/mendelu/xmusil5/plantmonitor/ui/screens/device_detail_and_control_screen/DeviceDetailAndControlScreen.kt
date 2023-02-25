package cz.mendelu.xmusil5.plantmonitor.ui.screens.device_detail_and_control_screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.icontio.senscare_peresonal_mobile.ui.components.screens.LoadingScreen
import cz.mendelu.xmusil5.plantmonitor.models.api.device.GetDevice
import cz.mendelu.xmusil5.plantmonitor.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantmonitor.ui.components.screens.ErrorScreen

@Composable
fun DeviceDetailAndControlScreen(
    deviceId: Long,
    navigation: INavigationRouter,
    viewModel: DeviceDetailAndControlViewModel = hiltViewModel()
){
    val device = remember{
        mutableStateOf<GetDevice?>(null)
    }
    val errorMessage = remember{
        mutableStateOf<String?>(null)
    }

    viewModel.uiState.value.let {
        when(it){
            is DeviceDetailAndControlUiState.Start -> {
                LoadingScreen()
                LaunchedEffect(it){
                    errorMessage.value = null
                    viewModel.fetchDevice(deviceId)
                }
            }
            is DeviceDetailAndControlUiState.DeviceLoaded -> {
                LaunchedEffect(it){
                    errorMessage.value = null
                    device.value = it.device
                }
                device.value?.let {
                    DeviceDetailAndControlScreenContent(
                        device = it,
                        errorMessage = errorMessage.value,
                        viewModel = viewModel
                    )
                }
            }
            is DeviceDetailAndControlUiState.DeviceUpdated -> {
                LaunchedEffect(it){
                    errorMessage.value = null
                    device.value = it.device
                }
                device.value?.let {
                    DeviceDetailAndControlScreenContent(
                        device = it,
                        errorMessage = errorMessage.value,
                        viewModel = viewModel
                    )
                }
            }
            is DeviceDetailAndControlUiState.DeviceUpdateFailed -> {
                val newErrorMessage = stringResource(id = it.errorMessageCode)
                LaunchedEffect(it, newErrorMessage){
                    errorMessage.value = newErrorMessage
                }
                device.value?.let {
                    DeviceDetailAndControlScreenContent(
                        device = it,
                        errorMessage = errorMessage.value,
                        viewModel = viewModel
                    )
                }
            }
            is DeviceDetailAndControlUiState.Error -> {
                ErrorScreen(text = stringResource(id = it.errorStringCode))
            }
        }
    }
}

@Composable
fun DeviceDetailAndControlScreenContent(
    device: GetDevice,
    errorMessage: String?,
    viewModel: DeviceDetailAndControlViewModel
){

}