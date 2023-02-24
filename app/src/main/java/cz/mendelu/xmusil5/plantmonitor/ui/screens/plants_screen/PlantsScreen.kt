package cz.mendelu.xmusil5.plantmonitor.ui.screens.plants_screen

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.icontio.senscare_peresonal_mobile.ui.components.screens.LoadingScreen
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.models.api.measurement.MeasurementValue
import cz.mendelu.xmusil5.plantmonitor.models.api.plant.GetPlant
import cz.mendelu.xmusil5.plantmonitor.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantmonitor.ui.components.list_items.PlantListItem
import cz.mendelu.xmusil5.plantmonitor.ui.components.screens.ErrorScreen
import cz.mendelu.xmusil5.plantmonitor.ui.components.screens.NoDataScreen
import cz.mendelu.xmusil5.plantmonitor.ui.components.ui_elements.AddFloatingActionButton

@Composable
fun PlantsScreen(
    navigation: INavigationRouter,
    viewModel: PlantsViewModel = hiltViewModel()
){
    val plants = remember{
        mutableStateListOf<GetPlant>()
    }

    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier
            .fillMaxSize()
    ) {
        viewModel.uiState.value.let {
            when (it) {
                is PlantsUiState.Start -> {
                    LaunchedEffect(it) {
                        viewModel.fetchPlants()
                    }
                    LoadingScreen()
                }
                is PlantsUiState.NoPlantsYet -> {
                    NoDataScreen(
                        message = stringResource(id = R.string.noPlantsYet),
                        iconId = R.drawable.ic_house_plant
                    )
                }
                is PlantsUiState.PlantsLoaded -> {
                    LaunchedEffect(it) {
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

        AddFloatingActionButton {
            navigation.toAddPlant()
        }
    }
}

@Composable
fun PlantsScreenContent(
    navigation: INavigationRouter,
    viewModel: PlantsViewModel,
    plants: List<GetPlant>
){
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ){
        items(
            count = plants.count(),
            key = {
                plants[it].id
            },
            itemContent = { index ->
                val plantImage = remember{
                    mutableStateOf(plants[index].titleImageBitmap)
                }
                val mostRecentMeasurementValues = remember {
                    mutableStateOf<List<MeasurementValue>?>(null)
                }
                LaunchedEffect(plants[index]){
                    if (plantImage.value == null) {
                        viewModel.fetchPlantImage(
                            plant = plants[index],
                            onSuccess = { image ->
                                plants[index].titleImageBitmap = image
                                plantImage.value = image
                            }
                        )
                    }
                }
                PlantListItem(
                    plant = plants[index],
                    plantImage = plantImage,
                    measurementValues = mostRecentMeasurementValues,
                    onExpanded = {
                        viewModel.fetchMostRecentValuesOfPlant(
                            plant = plants[index],
                            onValuesFetched = {
                                mostRecentMeasurementValues.value = it
                            }
                        )
                    },
                    onClick = {
                        navigation.toPlantDetail(plants[index].id)
                    },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                )
            }
        )
    }
}