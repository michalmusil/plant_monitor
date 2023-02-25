package cz.mendelu.xmusil5.plantmonitor.ui.screens.device_detail_and_control_screen

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.icontio.senscare_peresonal_mobile.ui.components.screens.LoadingScreen
import com.icontio.senscare_peresonal_mobile.ui.components.templates.TopBarWithBackButton
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.models.api.device.GetDevice
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementValue
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.GetPlant
import cz.mendelu.xmusil5.plantmonitor.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantmonitor.ui.components.complex_reusables.SwitchCard
import cz.mendelu.xmusil5.plantmonitor.ui.components.list_items.PlantListItem
import cz.mendelu.xmusil5.plantmonitor.ui.components.list_items.PlantListItemExpandable
import cz.mendelu.xmusil5.plantmonitor.ui.components.screens.ErrorScreen
import cz.mendelu.xmusil5.plantmonitor.ui.components.ui_elements.CustomButton
import cz.mendelu.xmusil5.plantmonitor.ui.components.ui_elements.ExpandableCard
import cz.mendelu.xmusil5.plantmonitor.ui.theme.errorColor
import cz.mendelu.xmusil5.plantmonitor.ui.theme.onErrorColor
import cz.mendelu.xmusil5.plantmonitor.ui.theme.onlineColor
import cz.mendelu.xmusil5.plantmonitor.ui.theme.shadowColor
import cz.mendelu.xmusil5.plantmonitor.utils.ImageUtils
import cz.mendelu.xmusil5.plantmonitor.utils.customShadow

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
    viewModel: DeviceDetailAndControlViewModel,
    navigation: INavigationRouter
){
    val imageCornerRadius = 10.dp

    Box(
        contentAlignment = Alignment.TopEnd,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            TopBarWithBackButton(
                topBarTitle = stringResource(
                    id = R.string.deviceDetailAndControlScreen
                ),
                onBackClick = {
                    navigation.returnBack()
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                contentAlignment = Alignment.TopEnd
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_measuring_device_filled),
                    contentDescription = stringResource(id = R.string.deviceImage),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(100.dp)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(imageCornerRadius))
                )
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(
                            if (device.active) onlineColor else errorColor
                        )
                )
            }

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
                text = device.uuid,
                style = MaterialTheme.typography.labelMedium,
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

        DeviceDetailAndControlLoadingIndicator(viewModel = viewModel)
    }
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
    val size = 30.dp
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(Color.Transparent)
            .padding(16.dp)
    ){
        AnimatedVisibility(
            visible = isShown.value,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .size(size)
            )
        }
    }
}

@Composable
fun DeviceDetailAndControlInfo(
    device: GetDevice,
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
    device: GetDevice,
    viewModel: DeviceDetailAndControlViewModel
){
    val cornerRadius = 30.dp
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
    plant: GetPlant,
    viewModel: DeviceDetailAndControlViewModel,
    navigation: INavigationRouter
){
    val cornerRadius = 30.dp

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
    device: GetDevice,
    viewModel: DeviceDetailAndControlViewModel
){
    val cornerRadius = 30.dp

    ExpandableCard(
        headlineContent = {
            Text(
                text = stringResource(id = R.string.assignToNewPlant),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface
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
            .background(MaterialTheme.colorScheme.background),
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
    device: GetDevice,
    viewModel: DeviceDetailAndControlViewModel
){
    val plantsToDisplay = remember {
        mutableStateListOf<GetPlant>()
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
                        },
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                    )
                }
            )
        }
    }
}