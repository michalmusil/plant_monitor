package cz.mendelu.xmusil5.plantmonitor.ui.screens.plants_screen

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.icontio.senscare_peresonal_mobile.ui.components.screens.LoadingScreen
import com.icontio.senscare_peresonal_mobile.ui.components.templates.ScreenSkeleton
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementValue
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.GetPlant
import cz.mendelu.xmusil5.plantmonitor.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantmonitor.ui.components.list_items.PlantListItem
import cz.mendelu.xmusil5.plantmonitor.ui.components.screens.ErrorScreen
import cz.mendelu.xmusil5.plantmonitor.ui.components.ui_elements.AddFloatingActionButton
import cz.mendelu.xmusil5.plantmonitor.ui.screens.add_device_screen.AddDeviceViewModel

@Composable
fun PlantsScreen(
    navigation: INavigationRouter,
    viewModel: PlantsViewModel = hiltViewModel()
){
    val plants = remember{
        mutableStateListOf<GetPlant>()
    }

    viewModel.uiState.value.let {
        when(it){
            is PlantsUiState.Start -> {
                LaunchedEffect(it){
                    viewModel.fetchPlants()
                }
                LoadingScreen()
            }
            is PlantsUiState.PlantsLoaded -> {
                LaunchedEffect(it){
                    plants.clear()
                    plants.addAll(it.plants)
                }
                PlantsScreenContent(
                    navigation = navigation,
                    viewModel = viewModel,
                    plants = plants
                )
            }
            is PlantsUiState.Error -> {
                ErrorScreen(text = stringResource(id = it.errorStringCode))
            }
        }
    }
}

@Composable
fun PlantsScreenContent(
    navigation: INavigationRouter,
    viewModel: PlantsViewModel,
    plants: List<GetPlant>
){
    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ){
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ){
            items(plants){
                val plantImage = remember{
                    mutableStateOf<Bitmap?>(null)
                }
                val mostRecentMeasurementValues = remember {
                    mutableStateOf(listOf<MeasurementValue>())
                }
                LaunchedEffect(it){
                    viewModel.fetchPlantImage(
                        plant = it,
                        onSuccess = {
                            plantImage.value = it
                        }
                    )
                }
                PlantListItem(
                    plant = it,
                    plantImage = plantImage,
                    measurementValues = mostRecentMeasurementValues,
                    onExpanded = {
                        viewModel.fetchMostRecentValuesOfPlant(
                            plant = it,
                            onValuesFetched = {
                                mostRecentMeasurementValues.value = it
                            }
                        )
                    },
                    onClick = {
                        navigation.toPlantDetail(it.id)
                    }
                )
            }
        }

        AddFloatingActionButton {
            navigation.toAddPlant()
        }
    }
}