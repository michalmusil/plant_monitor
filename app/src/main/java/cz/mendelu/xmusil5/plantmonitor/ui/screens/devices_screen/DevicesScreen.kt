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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.icontio.senscare_peresonal_mobile.ui.components.screens.LoadingScreen
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.models.api.device.Device
import cz.mendelu.xmusil5.plantmonitor.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantmonitor.ui.components.list_items.DeviceListItem
import cz.mendelu.xmusil5.plantmonitor.ui.components.screens.ErrorScreen
import cz.mendelu.xmusil5.plantmonitor.ui.components.screens.NoDataScreen
import cz.mendelu.xmusil5.plantmonitor.ui.components.ui_elements.AddFloatingActionButton
import cz.mendelu.xmusil5.plantmonitor.ui.components.ui_elements.CustomButton
import cz.mendelu.xmusil5.plantmonitor.ui.components.ui_elements.CustomTextField
import cz.mendelu.xmusil5.plantmonitor.ui.theme.errorColor
import cz.mendelu.xmusil5.plantmonitor.ui.theme.shadowColor
import cz.mendelu.xmusil5.plantmonitor.ui.utils.Edges
import cz.mendelu.xmusil5.plantmonitor.ui.utils.UiConstants
import cz.mendelu.xmusil5.plantmonitor.utils.customShadow
import cz.mendelu.xmusil5.plantmonitor.utils.fadeEdges

@Composable
fun DevicesScreen(
    navigation: INavigationRouter,
    viewModel: DevicesViewModel = hiltViewModel()
){
    val devices = remember{
        mutableStateListOf<Device>()
    }

    val showAddDialog = remember{
        mutableStateOf(false)
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
                        showAddDialog.value = false
                        viewModel.fetchDevices()
                    }
                }
                is DevicesUiState.NoDevicesYet -> {
                    LaunchedEffect(it){
                        showAddDialog.value = false
                    }
                    NoDataScreen(
                        message = stringResource(id = R.string.noDevicesYet),
                        iconId = R.drawable.ic_measuring_device
                    )
                }
                is DevicesUiState.DevicesLoaded -> {
                    LaunchedEffect(it) {
                        showAddDialog.value = false
                        devices.clear()
                        devices.addAll(it.devices)
                    }
                    DevicesScreenContent(
                        navigation = navigation,
                        viewModel = viewModel,
                        devices = devices,
                        showAddDialog = showAddDialog
                    )
                }
                is DevicesUiState.Error -> {
                    LaunchedEffect(it){
                        showAddDialog.value = false
                    }
                    ErrorScreen(text = stringResource(id = it.errorStringCode)){
                        viewModel.uiState.value = DevicesUiState.Start()
                    }
                }
            }
        }

        AddFloatingActionButton {
            showAddDialog.value = true
        }

        NewDeviceDialog(
            showDialog = showAddDialog,
            viewModel = viewModel
        )
    }
}

@Composable
fun DevicesScreenContent(
    navigation: INavigationRouter,
    viewModel: DevicesViewModel,
    devices: List<Device>,
    showAddDialog: MutableState<Boolean>
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
                fadeWidth = UiConstants.EDGE_FADE_MEDIUM
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
    val cornerRadius = UiConstants.RADIUS_LARGE
    val errorTextDefault = stringResource(id = R.string.deviceRegistrationFailed)

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
    val displayedError = remember{
        mutableStateOf<String?>(null)
    }
    LaunchedEffect(showDialog.value){
        communicationIdentifier.value = ""
        macAddress.value = ""
        displayedError.value = null
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
                    .clip(RoundedCornerShape(cornerRadius))
                    .customShadow(
                        color = shadowColor,
                        borderRadius = cornerRadius,
                        spread = 0.dp,
                        blurRadius = 5.dp,
                        offsetY = 2.dp
                    )
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)
            ){
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                ){
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_plus),
                            contentDescription = stringResource(id = R.string.icon),
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .size(30.dp)
                        )
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_measuring_device_filled),
                            contentDescription = stringResource(id = R.string.deviceImage),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(40.dp)
                        )
                    }
                }

                Text(
                    text = stringResource(id = R.string.addNewDevice),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 2,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                )

                CustomTextField(
                    labelTitle = stringResource(id = R.string.newDeviceIdentifier),
                    value = communicationIdentifier,
                    maxChars = 15,
                    singleLine = true,
                    isError = communicationIdentifierError.value,
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                        imeAction = ImeAction.Next
                    ),
                    errorMessage = stringResource(id = R.string.communicationIdentifierNotInCorrectFormat),
                    onTextChanged = {
                        communicationIdentifierError.value = !viewModel.stringValidator.isCommunicationIdentifierValid(
                            communicationIdentifier = it
                        )
                        displayedError.value = null
                    }
                )

                CustomTextField(
                    labelTitle = stringResource(id = R.string.newDeviceMacAddress),
                    value = macAddress,
                    maxChars = 17,
                    singleLine = true,
                    isError = macAddressError.value,
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                        imeAction = ImeAction.Done
                    ),
                    errorMessage = stringResource(id = R.string.macAddressNotInCorrectFormat),
                    onTextChanged = {
                        macAddressError.value = !viewModel.stringValidator.isMacAddressValid(
                            macAddress = it
                        )
                        displayedError.value = null
                    }
                )

                Spacer(modifier = Modifier.height(5.dp))

                displayedError.value?.let {
                    Text(
                        text = it,
                        color = errorColor,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                val buttonEnabled = !communicationIdentifierError.value && !macAddressError.value
                        && communicationIdentifier.value.isNotBlank() && macAddress.value.isNotBlank()
                CustomButton(
                    text = stringResource(id = R.string.add),
                    backgroundColor = MaterialTheme.colorScheme.secondary,
                    iconId = R.drawable.ic_plus,
                    enabled = buttonEnabled,
                    onClick = {
                        viewModel.addNewDevice(
                            communicationIdentifier = communicationIdentifier.value,
                            macAddress = macAddress.value,
                            onError = {
                                displayedError.value = errorTextDefault
                            }
                        )
                    }
                )

                Spacer(modifier = Modifier.height(35.dp))
            }
        }
    }
}