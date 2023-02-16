package cz.mendelu.xmusil5.plantmonitor.ui.screens.profile_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.icontio.senscare_peresonal_mobile.ui.components.templates.ScreenSkeleton
import cz.mendelu.xmusil5.plantmonitor.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantmonitor.ui.screens.add_device_screen.AddDeviceViewModel

@Composable
fun ProfileScreen(
    navigation: INavigationRouter,
    viewModel: ProfileViewModel = hiltViewModel()
){
    Column {
        Text(text = "profile screen!!")
        Button(
            onClick = {
                navigation.toLogin()
            }
        ) {
            Text(text = "Logout")
        }
    }

}