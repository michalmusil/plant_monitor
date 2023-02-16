package cz.mendelu.xmusil5.plantmonitor.ui.screens.login_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.icontio.senscare_peresonal_mobile.ui.components.templates.ScreenSkeleton
import cz.mendelu.xmusil5.plantmonitor.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantmonitor.ui.screens.add_device_screen.AddDeviceViewModel

@Composable
fun LoginScreen(
    navigation: INavigationRouter,
    viewModel: LoginViewModel = hiltViewModel()
){
    Column {
        Text(text = "login screen!!")
        Button(
            onClick = {
                navigation.toPlantsScreen()
            }
        ) {
            Text(text = "Log in")
        }
    }
}