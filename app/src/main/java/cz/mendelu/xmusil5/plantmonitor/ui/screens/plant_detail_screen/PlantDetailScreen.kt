package cz.mendelu.xmusil5.plantmonitor.ui.screens.plant_detail_screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.icontio.senscare_peresonal_mobile.ui.components.templates.ScreenSkeleton
import cz.mendelu.xmusil5.plantmonitor.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantmonitor.ui.screens.add_device_screen.AddDeviceViewModel

@Composable
fun PlantDetailScreen(
    plantId: Long,
    navigation: INavigationRouter,
    viewModel: PlantDetailViewModel = hiltViewModel()
){
    ScreenSkeleton(
        navigation = navigation,
        content = {
            Text(text = "plant detail screen!!")
        }
    )
}