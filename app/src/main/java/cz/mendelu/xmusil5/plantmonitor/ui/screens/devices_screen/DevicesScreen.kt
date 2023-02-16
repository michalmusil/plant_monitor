package cz.mendelu.xmusil5.plantmonitor.ui.screens.devices_screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.icontio.senscare_peresonal_mobile.ui.components.templates.ScreenSkeleton
import cz.mendelu.xmusil5.plantmonitor.navigation.INavigationRouter

@Composable
fun DevicesScreen(
    navigation: INavigationRouter,
    viewModel: DevicesViewModel = hiltViewModel()
){
    Text(text = "devices screen!!")
}