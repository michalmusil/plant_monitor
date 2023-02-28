package cz.mendelu.xmusil5.plantmonitor.ui.screens.devices_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.icontio.senscare_peresonal_mobile.ui.components.screens.LoadingScreen
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.models.api.device.GetDevice
import cz.mendelu.xmusil5.plantmonitor.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantmonitor.ui.components.list_items.DeviceListItem
import cz.mendelu.xmusil5.plantmonitor.ui.components.screens.ErrorScreen
import cz.mendelu.xmusil5.plantmonitor.ui.components.screens.NoDataScreen
import cz.mendelu.xmusil5.plantmonitor.ui.components.ui_elements.AddFloatingActionButton

@Composable
fun DevicesScreen(
    navigation: INavigationRouter,
    viewModel: DevicesViewModel = hiltViewModel()
){
    val devices = remember{
        mutableStateListOf<GetDevice>()
    }
    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier
            .fillMaxSize()
    ) {
        viewModel.uiState.value.let {
            when (it) {
                is DevicesUiState.Start -> {
                    LoadingScreen()
                    LaunchedEffect(it) {
                        viewModel.fetchDevices()
                    }
                }
                is DevicesUiState.NoDevicesYet -> {
                    NoDataScreen(
                        message = stringResource(id = R.string.noDevicesYet),
                        iconId = R.drawable.ic_questionmark
                    )
                }
                is DevicesUiState.DevicesLoaded -> {
                    LaunchedEffect(it) {
                        devices.clear()
                        devices.addAll(it.devices)
                    }
                    DevicesScreenContent(
                        navigation = navigation,
                        viewModel = viewModel,
                        devices = devices
                    )
                }
                is DevicesUiState.Error -> {
                    ErrorScreen(text = stringResource(id = it.errorStringCode)){
                        viewModel.uiState.value = DevicesUiState.Start()
                    }
                }
            }
        }

        AddFloatingActionButton {
            navigation.toAddDevice()
        }
    }
}

@Composable
fun DevicesScreenContent(
    navigation: INavigationRouter,
    viewModel: DevicesViewModel,
    devices: List<GetDevice>
){
    LazyVerticalGrid(
        columns = GridCells.Adaptive(170.dp),
        contentPadding = PaddingValues(vertical = 10.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        items(
            count = devices.size,
            key = {
                devices[it].id
            },
            itemContent = { index ->
                DeviceListItem(
                    device = devices[index],
                    onClick = {
                        navigation.toDeviceDetailAndControl(
                            deviceId = devices[index].id
                        )
                    }
                )
            }
        )
    }
}