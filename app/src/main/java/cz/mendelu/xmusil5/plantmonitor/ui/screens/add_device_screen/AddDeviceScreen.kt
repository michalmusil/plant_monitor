package cz.mendelu.xmusil5.plantmonitor.ui.screens.add_device_screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.icontio.senscare_peresonal_mobile.ui.components.templates.ScreenSkeleton
import cz.mendelu.xmusil5.plantmonitor.navigation.INavigationRouter


@Composable
fun AddDeviceScreen(
    navigation: INavigationRouter,
    viewModel: AddDeviceViewModel = hiltViewModel()
){
    ScreenSkeleton(
        navigation = navigation,
        content = {
            Text(text = "add device screen!!")
        }
    )
}