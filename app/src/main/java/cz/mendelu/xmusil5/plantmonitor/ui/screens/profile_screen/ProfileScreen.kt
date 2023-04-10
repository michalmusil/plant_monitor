package cz.mendelu.xmusil5.plantmonitor.ui.screens.profile_screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.icontio.senscare_peresonal_mobile.ui.components.screens.LoadingScreen
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.models.api.user.User
import cz.mendelu.xmusil5.plantmonitor.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantmonitor.ui.components.complex_reusables.SwitchCard
import cz.mendelu.xmusil5.plantmonitor.ui.components.screens.ErrorScreen
import cz.mendelu.xmusil5.plantmonitor.ui.components.ui_elements.CustomButton
import cz.mendelu.xmusil5.plantmonitor.ui.components.ui_elements.SmallLoadingIndicator
import cz.mendelu.xmusil5.plantmonitor.ui.theme.shadowColor
import cz.mendelu.xmusil5.plantmonitor.ui.utils.UiConstants
import cz.mendelu.xmusil5.plantmonitor.utils.customShadow

@Composable
fun ProfileScreen(
    navigation: INavigationRouter,
    viewModel: ProfileViewModel = hiltViewModel()
){
    viewModel.uiState.value.let {
        when (it) {
            is ProfileUiState.Start -> {
                LaunchedEffect(it) {
                    viewModel.loadData()
                }
                LoadingScreen()
            }
            is ProfileUiState.UserLoaded -> {
                ProfileScreenContent(
                    user = it.user,
                    viewModel = viewModel,
                    navigation = navigation
                )
            }
            is ProfileUiState.Error -> {
                ErrorScreen(text = stringResource(id = it.errorStringCode)) {
                    viewModel.uiState.value = ProfileUiState.Start()
                }
            }
        }
    }
}

@Composable
fun ProfileScreenContent(
    user: User,
    viewModel: ProfileViewModel,
    navigation: INavigationRouter
){
    Box(
        contentAlignment = Alignment.TopEnd,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            UpperProfileScreenPart(
                user = user,
                viewModel = viewModel
            )

            LowerProfileScreenPart(
                user = user,
                viewModel = viewModel
            )
        }
        ProfileScreenLoadingIndicator(viewModel = viewModel)
    }
}

@Composable
fun ProfileScreenLoadingIndicator(
    viewModel: ProfileViewModel
){
    val isShown = remember{
        mutableStateOf(false)
    }
    LaunchedEffect(viewModel.isLoading.value){
        isShown.value = viewModel.isLoading.value
    }
    SmallLoadingIndicator(
        isShown = isShown
    )
}

@Composable
fun UpperProfileScreenPart(
    user: User,
    viewModel: ProfileViewModel,
){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(130.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            ){
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_farmer),
                    contentDescription = stringResource(id = R.string.userIcon),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .padding(35.dp)
                )
            }

            Text(
                text = user.email,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = stringResource(id = user.role.roleNameId),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(15.dp))
            
            CustomButton(
                text = stringResource(id = R.string.logout),
                backgroundColor = MaterialTheme.colorScheme.secondary,
                textColor = MaterialTheme.colorScheme.onSecondary,
                onClick = {
                    viewModel.logOut()
                }
            )
        }
    }
}

@Composable
fun LowerProfileScreenPart(
    user: User,
    viewModel: ProfileViewModel,
){
    val cornerRadius = UiConstants.RADIUS_LARGE

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .offset(y = (-25).dp)
            .fillMaxSize()
            .clip(RoundedCornerShape(topStart = cornerRadius, topEnd = cornerRadius))
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 30.dp)
    ) {
        
        NotificationEnabledSwitch(
            viewModel = viewModel,
            user = user
        )
        
        Spacer(modifier = Modifier.height(20.dp))
        
        DarkModeEnabledSwitch(
            viewModel = viewModel
        )
    }
}

@Composable
fun NotificationEnabledSwitch(
    viewModel: ProfileViewModel,
    user: User
){
    val cornerRadius = UiConstants.RADIUS_LARGE

    val enabledShort = stringResource(id = R.string.notificationsEnabledShort)
    val disabledShort = stringResource(id = R.string.notificationsDisabledShort)
    val enabledLong = stringResource(id = R.string.notificationsEnabledLong)
    val disabledLong = stringResource(id = R.string.notificationsDisabledLong)
    val enabled = remember{
        mutableStateOf(viewModel.notificationsEnabled.value)
    }
    val textShort = remember{
        mutableStateOf("")
    }
    val textLong = remember{
        mutableStateOf("")
    }
    viewModel.notificationsEnabled.value.let {
        LaunchedEffect(it){
            enabled.value = it
            when(it){
                true -> {
                    textShort.value = enabledShort
                    textLong.value = enabledLong
                }
                false -> {
                    textShort.value = disabledShort
                    textLong.value = disabledLong
                }
            }
        }
    }
    
    SwitchCard(
        checked = enabled,
        mainText = textShort.value,
        secondaryText = textLong.value,
        iconId = R.drawable.ic_notification,
        onValueChange = {
            viewModel.setNotificationsEnabled(
                enabled = it,
                user = user
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

@Composable
fun DarkModeEnabledSwitch(
    viewModel: ProfileViewModel
){
    val darkModeSystemDefault = isSystemInDarkTheme()
    val cornerRadius = UiConstants.RADIUS_LARGE

    val enabledString = stringResource(id = R.string.darkModeEnabled)
    val disabledString = stringResource(id = R.string.darkModeDisabled)
    val enabled = remember{
        mutableStateOf(false)
    }
    val text = remember{
        mutableStateOf("")
    }
    val isDarkModeManuallyOverriden = remember{
        mutableStateOf(false)
    }

    viewModel.darkModePreference.value.let {
        LaunchedEffect(it){
            if (it != null){
                isDarkModeManuallyOverriden.value = true
                enabled.value = it
            } else {
                isDarkModeManuallyOverriden.value = false
                enabled.value = darkModeSystemDefault
            }
            when(enabled.value){
                true -> {
                    text.value = enabledString
                }
                false -> {
                    text.value = disabledString
                }
            }
        }
    }

    SwitchCard(
        checked = enabled,
        mainText = text.value,
        iconId = R.drawable.ic_dark_mode,
        onValueChange = {
            viewModel.overrideDarkModePreference(
                darkMode = it
            )
        },
        additionalContent = {
            if (isDarkModeManuallyOverriden.value){
                CustomButton(
                    text = stringResource(id = R.string.revertToSystemDefaults),
                    backgroundColor = MaterialTheme.colorScheme.secondary,
                    textColor = MaterialTheme.colorScheme.onSecondary,
                    textSize = 12.sp,
                    onClick = {
                        viewModel.overrideDarkModePreference(null)
                    }
                )
            }
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