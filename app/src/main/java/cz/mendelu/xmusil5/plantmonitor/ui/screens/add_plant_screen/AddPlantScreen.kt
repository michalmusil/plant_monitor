package cz.mendelu.xmusil5.plantmonitor.ui.screens.add_plant_screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.icontio.senscare_peresonal_mobile.ui.components.templates.ScreenSkeleton
import cz.mendelu.xmusil5.plantmonitor.navigation.INavigationRouter

@Composable
fun AddPlantScreen(
    navigation: INavigationRouter,
    viewModel: AddPlantViewModel = hiltViewModel()
){
    ScreenSkeleton(
        navigation = navigation,
        content = {
            Text(text = "add plant screen!!")
        }
    )
}