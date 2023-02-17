package cz.mendelu.xmusil5.plantmonitor.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.icontio.senscare_peresonal_mobile.ui.components.templates.ScreenSkeleton
import cz.mendelu.xmusil5.plantmonitor.authentication.IAuthenticationManager
import cz.mendelu.xmusil5.plantmonitor.ui.screens.add_device_screen.AddDeviceScreen
import cz.mendelu.xmusil5.plantmonitor.ui.screens.add_plant_screen.AddPlantScreen
import cz.mendelu.xmusil5.plantmonitor.ui.screens.device_detail_screen.DeviceDetailScreen
import cz.mendelu.xmusil5.plantmonitor.ui.screens.devices_screen.DevicesScreen
import cz.mendelu.xmusil5.plantmonitor.ui.screens.login_screen.LoginScreen
import cz.mendelu.xmusil5.plantmonitor.ui.screens.plant_detail_screen.PlantDetailScreen
import cz.mendelu.xmusil5.plantmonitor.ui.screens.plants_screen.PlantsScreen
import cz.mendelu.xmusil5.plantmonitor.ui.screens.profile_screen.ProfileScreen


@Composable
fun NavGraph(
    authenticationManager: IAuthenticationManager,
    navController: NavHostController = rememberNavController(),
    navigation: INavigationRouter,
    startDestination: String
) {
    var navigationInitialized by remember{
        mutableStateOf(false)
    }

    LaunchedEffect(true){
        navigation.setNavController(navController)
        navigationInitialized = true
    }

    if (navigationInitialized) {
        ScreenSkeleton(
            navigation = navigation,
            content = {
                NavHost(
                    navController = navController,
                    startDestination = startDestination
                ) {
                    composable(Destination.LoginScreen.route) {
                        LoginScreen(navigation = navigation)
                    }

                    composable(Destination.PlantsScreen.route) {
                        PlantsScreen(navigation = navigation)
                    }

                    composable(Destination.DevicesScreen.route) {
                        DevicesScreen(navigation = navigation)
                    }

                    composable(Destination.ProfileScreen.route) {
                        ProfileScreen(navigation = navigation)
                    }

                    composable(Destination.PlantDetailScreen.route + "/{plantId}",
                        arguments = listOf(
                            navArgument("plantId") {
                                type = NavType.IntType
                                defaultValue = -1
                            }
                        )) {
                        val plantId = it.arguments?.getInt("plantId") ?: -1
                        PlantDetailScreen(plantId = plantId, navigation = navigation)
                    }

                    composable(Destination.DeviceDetailScreen.route + "/{deviceId}",
                        arguments = listOf(
                            navArgument("deviceId") {
                                type = NavType.IntType
                                defaultValue = -1
                            }
                        )) {
                        val deviceId = it.arguments?.getInt("deviceId") ?: -1
                        DeviceDetailScreen(deviceId = deviceId, navigation = navigation)
                    }

                    composable(Destination.AddPlantScreen.route) {
                        AddPlantScreen(navigation = navigation)
                    }

                    composable(Destination.AddDeviceScreen.route) {
                        AddDeviceScreen(navigation = navigation)
                    }
                }
            }
        )
    }
}