package cz.mendelu.xmusil5.plantmonitor.ui.screens.profile_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.icontio.senscare_peresonal_mobile.ui.components.screens.LoadingScreen
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.models.api.user.GetUser
import cz.mendelu.xmusil5.plantmonitor.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantmonitor.ui.components.screens.ErrorScreen
import cz.mendelu.xmusil5.plantmonitor.ui.components.ui_elements.CustomButton
import cz.mendelu.xmusil5.plantmonitor.ui.components.ui_elements.SmallLoadingIndicator

@Composable
fun ProfileScreen(
    navigation: INavigationRouter,
    viewModel: ProfileViewModel = hiltViewModel()
){
    viewModel.uiState.value.let {
        when(it){
            is ProfileUiState.Start -> {
                LaunchedEffect(it){
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
    user: GetUser,
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
    user: GetUser,
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
    user: GetUser,
    viewModel: ProfileViewModel
){
    val cornerRadius = 30.dp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .offset(y = (-25).dp)
            .fillMaxSize()
            .clip(RoundedCornerShape(topStart = cornerRadius, topEnd = cornerRadius))
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        Text("Fill this part out later")
        Text("Fill this part out later")
        Text("Fill this part out later")
        Text("Fill this part out later")
        Text("Fill this part out later")
        Text("Fill this part out later")
        Text("Fill this part out later")
        Text("Fill this part out later")
        Text("Fill this part out later")
    }
}

