package cz.mendelu.xmusil5.plantmonitor.ui.screens.devices_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.icontio.senscare_peresonal_mobile.ui.components.screens.LoadingScreen
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.models.api.device.GetDevice
import cz.mendelu.xmusil5.plantmonitor.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantmonitor.ui.components.list_items.DeviceListItem
import cz.mendelu.xmusil5.plantmonitor.ui.components.screens.ErrorScreen
import cz.mendelu.xmusil5.plantmonitor.ui.components.screens.NoDataScreen
import cz.mendelu.xmusil5.plantmonitor.ui.components.ui_elements.AddFloatingActionButton
import cz.mendelu.xmusil5.plantmonitor.ui.theme.shadowColor
import cz.mendelu.xmusil5.plantmonitor.ui.utils.Edges
import cz.mendelu.xmusil5.plantmonitor.utils.customShadow
import cz.mendelu.xmusil5.plantmonitor.utils.fadeEdges

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
        contentPadding = PaddingValues(
            top = 16.dp,
            bottom = 50.dp,
            start = 16.dp,
            end = 16.dp
        ),
        modifier = Modifier
            .fillMaxSize()
            .fadeEdges(
                edges = Edges.VERTICAL,
                backgroundColor = MaterialTheme.colorScheme.background,
                fadeWidth = 100f
            )
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

@Composable
fun NewDeviceDialog(
    showDialog: MutableState<Boolean>,
    viewModel: DevicesViewModel
){
    val animationDuration = 150
    val cornerRadius = 30.dp

    val communicationIdentifier = remember{
        mutableStateOf("")
    }
    val macAddress = remember{
        mutableStateOf("")
    }
    val communicationIdentifierError = remember{
        mutableStateOf(false)
    }
    val macAddressError = remember{
        mutableStateOf(false)
    }
    LaunchedEffect(showDialog.value){
        communicationIdentifier.value = ""
        macAddress.value = ""
        communicationIdentifierError.value = false
        macAddressError.value = false
    }

    AnimatedVisibility(
        visible = showDialog.value,
        enter = fadeIn(animationSpec = tween(animationDuration)),
        exit = fadeOut(animationSpec = tween(animationDuration))
    ) {
        Dialog(
            onDismissRequest = {
                showDialog.value = false
            }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(cornerRadius))
                    .customShadow(
                        color = shadowColor,
                        borderRadius = cornerRadius,
                        spread = 0.dp,
                        blurRadius = 5.dp,
                        offsetY = 2.dp
                    )
            ){
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                ){
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_measuring_device_filled),
                            contentDescription = stringResource(id = R.string.deviceImage),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(40.dp)
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_plus),
                            contentDescription = stringResource(id = R.string.icon),
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .size(30.dp)
                        )
                    }
                }



            }
        }
    }
}