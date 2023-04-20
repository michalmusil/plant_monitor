package cz.mendelu.xmusil5.plantmonitor.ui.screens.device_detail_and_control_screen

import android.graphics.Bitmap
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.icontio.senscare_peresonal_mobile.ui.components.screens.LoadingScreen
import com.icontio.senscare_peresonal_mobile.ui.components.templates.TopBarWithBackButton
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.models.api.device.Device
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.Plant
import cz.mendelu.xmusil5.plantmonitor.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantmonitor.ui.components.complex_reusables.SwitchCard
import cz.mendelu.xmusil5.plantmonitor.ui.components.list_items.PlantListItem
import cz.mendelu.xmusil5.plantmonitor.ui.components.screens.ErrorScreen
import cz.mendelu.xmusil5.plantmonitor.ui.components.templates.PopupDialog
import cz.mendelu.xmusil5.plantmonitor.ui.components.ui_elements.CircularIconButton
import cz.mendelu.xmusil5.plantmonitor.ui.components.ui_elements.ExpandableCard
import cz.mendelu.xmusil5.plantmonitor.ui.components.ui_elements.SmallLoadingIndicator
import cz.mendelu.xmusil5.plantmonitor.ui.theme.errorColor
import cz.mendelu.xmusil5.plantmonitor.ui.theme.onlineColor
import cz.mendelu.xmusil5.plantmonitor.ui.theme.shadowColor
import cz.mendelu.xmusil5.plantmonitor.ui.tutorials.TutorialDeviceWifiConnect
import cz.mendelu.xmusil5.plantmonitor.ui.utils.UiConstants
import cz.mendelu.xmusil5.plantmonitor.utils.image.ImageUtils
import cz.mendelu.xmusil5.plantmonitor.utils.customShadow

@Composable
fun DeviceDetailAndControlScreen(
    deviceId: Long,
    navigation: INavigationRouter,
    viewModel: DeviceDetailAndControlViewModel = hiltViewModel()
){
    val device = remember{
        mutableStateOf<Device?>(null)
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
                        viewModel = viewModel,
                        navigation = navigation
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
                        viewModel = viewModel,
                        navigation = navigation
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
                        viewModel = viewModel,
                        navigation = navigation
                    )
                }
            }
            is DeviceDetailAndControlUiState.DeviceUnregistered -> {
                LaunchedEffect(it){
                    navigation.toDevicesScreen()
                }
            }
            is DeviceDetailAndControlUiState.Error -> {
                ErrorScreen(text = stringResource(id = it.errorStringCode)){
                    viewModel.uiState.value = DeviceDetailAndControlUiState.Start()
                }
            }
        }
    }
}

@Composable
fun DeviceDetailAndControlScreenContent(
    device: Device,
    errorMessage: String?,
    viewModel: DeviceDetailAndControlViewModel,
    navigation: INavigationRouter
){
    val showUnregisterDialog = rememberSaveable{
        mutableStateOf(false)
    }

    val showDeviceWifiTutorial = rememberSaveable{
        mutableStateOf(false)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        TopBarWithBackButton(
            topBarTitle = stringResource(R.string.deviceDetailAndControlScreen),
            actions = {
                IconButton(
                    onClick = {
                        showUnregisterDialog.value = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = stringResource(id = R.string.unregister),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            onBackClick = {
                navigation.returnBack()
            }
        )

        DeviceUnregisterDialog(
            device = device,
            showDialog = showUnregisterDialog,
            viewModel = viewModel
        )

        Box(
            contentAlignment = Alignment.TopStart,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                DeviceDetailAndControlIcon(device = device)
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .fillMaxWidth()
            ){
                DeviceDetailAndControlWifiTutorial(showDeviceWifiTutorial)
                DeviceDetailAndControlLoadingIndicator(viewModel = viewModel)
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            errorMessage?.let {
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = errorColor
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = device.getDisplayName(context = LocalContext.current),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp))
            DeviceDetailAndControlInfo(
                device = device,
                viewModel = viewModel,
                navigation = navigation
            )
        }
    }
}

@Composable
fun DeviceUnregisterDialog(
    device: Device,
    showDialog: MutableState<Boolean>,
    viewModel: DeviceDetailAndControlViewModel
){
    PopupDialog(
        showDialog = showDialog,
        title = stringResource(id = R.string.unregisterDevice),
        text = stringResource(id = R.string.sureToUnregisterDevice),
        confirmButtonText = stringResource(id = R.string.yes),
        cancelButtonText = stringResource(id = R.string.no),
        onConfirm = {
            viewModel.unregisterDevice(deviceId = device.id)
            showDialog.value = false
        },
        onCancelOrDismiss = {
            showDialog.value = false
        }
    )
}

@Composable
fun DeviceDetailAndControlLoadingIndicator(
    viewModel: DeviceDetailAndControlViewModel
){
    val isShown = remember{
        mutableStateOf(false)
    }
    viewModel.isLoading.value.let {
        LaunchedEffect(it){
            isShown.value = it
        }
    }
    SmallLoadingIndicator(
        isShown = isShown
    )
}

@Composable
fun DeviceDetailAndControlWifiTutorial(
    showTutorial: MutableState<Boolean>
){
    CircularIconButton(
        iconId = R.drawable.ic_questionmark,
        size = 55.dp,
        backgroundColor = MaterialTheme.colorScheme.secondary,
        foregroundColor = MaterialTheme.colorScheme.onSecondary,
        onClick = {
            showTutorial.value = true
        }
    )
    TutorialDeviceWifiConnect(
        showTutorial = showTutorial
    )
}

@Composable
fun DeviceDetailAndControlIcon(
    device: Device
){
    Box(
        contentAlignment = Alignment.TopEnd,
        modifier = Modifier
            .size(180.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_measuring_device_filled),
                contentDescription = stringResource(id = R.string.deviceImage),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(23.dp)
            )
        }
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(
                    if (device.active) onlineColor else errorColor
                )
        )
    }
}

@Composable
fun DeviceDetailAndControlInfo(
    device: Device,
    viewModel: DeviceDetailAndControlViewModel,
    navigation: INavigationRouter
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        DeviceActivationSwitch(
            device = device,
            viewModel = viewModel
        )

        device.plant?.let {
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
            ){
                CurrentlyAssignedPlantCard(
                    plant = it,
                    viewModel = viewModel,
                    navigation = navigation
                )
            }
        }

        AssignNewPlantCard(
            device = device,
            viewModel = viewModel
        )
    }
}



@Composable
fun DeviceActivationSwitch(
    device: Device,
    viewModel: DeviceDetailAndControlViewModel
){
    val cornerRadius = UiConstants.RADIUS_LARGE
    val mainActive = stringResource(id = R.string.deviceIsActivePrimary)
    val secondaryActive = stringResource(id = R.string.deviceIsActiveSecondary)
    val mainInactive = stringResource(id = R.string.deviceIsInactivePrimary)
    val secondaryInactive = stringResource(id = R.string.deviceIsInactiveSecondary)

    val active = remember {
        mutableStateOf(device.active)
    }
    val mainText = remember{
        mutableStateOf("")
    }
    val secondaryText = remember{
        mutableStateOf("")
    }

    LaunchedEffect(device){
        active.value = device.active
        when(active.value){
            true -> {
                mainText.value = mainActive
                secondaryText.value = secondaryActive
            }
            false -> {
                mainText.value = mainInactive
                secondaryText.value = secondaryInactive
            }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        SwitchCard(
            checked = active,
            mainText = mainText.value,
            secondaryText = secondaryText.value,
            onValueChange = {
                viewModel.proceedDeviceActivation(
                    active = it,
                    deviceId = device.id
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .customShadow(
                    color = shadowColor,
                    borderRadius = cornerRadius,
                    spread = 0.dp,
                    blurRadius = 5.dp,
                    offsetY = 2.dp
                )
                .clip(RoundedCornerShape(cornerRadius))
                .background(MaterialTheme.colorScheme.surface)
        )
    }
}

@Composable
fun CurrentlyAssignedPlantCard(
    plant: Plant,
    viewModel: DeviceDetailAndControlViewModel,
    navigation: INavigationRouter
){
    val cornerRadius = UiConstants.RADIUS_LARGE

    val plantImage = remember{
        mutableStateOf<Bitmap?>(null)
    }
    LaunchedEffect(plant){
        if(plant.hasTitleImage) {
            viewModel.fetchPlantImage(
                plant = plant,
                onSuccess = {
                    plantImage.value = it
                }
            )
        }
        else{
            plantImage.value = null
        }
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .customShadow(
                color = shadowColor,
                borderRadius = cornerRadius,
                spread = 0.dp,
                blurRadius = 5.dp,
                offsetY = 2.dp
            )
            .clip(RoundedCornerShape(cornerRadius))
            .background(MaterialTheme.colorScheme.surface)
            .clickable {
                navigation.toPlantDetail(plant.id)
            }
            .padding(16.dp)
    ){
        Image(
            bitmap = plantImage.value?.asImageBitmap()
                ?: ImageUtils.getBitmapFromVectorDrawable(
                    LocalContext.current, R.drawable.ic_plant_root
                )!!.asImageBitmap(),
            contentDescription = stringResource(id = R.string.plantImage),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(50.dp)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(20.dp))
        )
        
        Spacer(modifier = Modifier.width(10.dp))

        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_house_plant),
                    contentDescription = stringResource(id = R.string.icon),
                    modifier = Modifier
                        .size(22.dp)
                )

                Spacer(modifier = Modifier.width(2.dp))

                Text(
                    text = stringResource(id = R.string.assignedToPlant),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = plant.name,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun AssignNewPlantCard(
    device: Device,
    viewModel: DeviceDetailAndControlViewModel
){
    val cornerRadius = UiConstants.RADIUS_LARGE

    ExpandableCard(
        headlineContent = {
            Text(
                text = stringResource(id = R.string.assignToNewPlant),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSecondary
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .customShadow(
                color = shadowColor,
                borderRadius = cornerRadius,
                spread = 0.dp,
                blurRadius = 5.dp,
                offsetY = 2.dp
            )
            .clip(RoundedCornerShape(cornerRadius))
            .background(MaterialTheme.colorScheme.secondary),
        expandedContent = {
            AssignablePlantsList(
                device = device,
                viewModel = viewModel
            )
        }
    )
}

@Composable
fun AssignablePlantsList(
    device: Device,
    viewModel: DeviceDetailAndControlViewModel
){
    val plantsToDisplay = remember {
        mutableStateListOf<Plant>()
    }
    LaunchedEffect(device){
        viewModel.fetchPlants {
            val withCurrentPlantRemoved = it.filter {
                it.id != device.plantId
            }
            plantsToDisplay.clear()
            plantsToDisplay.addAll(withCurrentPlantRemoved)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
        ) {
            items(
                count = plantsToDisplay.count(),
                key = {
                    plantsToDisplay[it].id
                },
                itemContent = { index ->
                    val plant = plantsToDisplay[index]

                    val plantImage = remember {
                        mutableStateOf(plant.titleImageBitmap)
                    }
                    LaunchedEffect(plant) {
                        if (plantImage.value == null) {
                            viewModel.fetchPlantImage(
                                plant = plant,
                                onSuccess = { image ->
                                    plant.titleImageBitmap = image
                                    plantImage.value = image
                                }
                            )
                        }
                    }
                    PlantListItem(
                        plant = plant,
                        plantImage = plantImage,
                        onClick = {
                            viewModel.assignDeviceToPlant(
                                deviceId = device.id,
                                plantId = plant.id
                            )
                        }
                    )
                }
            )
        }
    }
}