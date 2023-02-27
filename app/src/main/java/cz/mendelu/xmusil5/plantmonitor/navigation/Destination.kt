package cz.mendelu.xmusil5.plantmonitor.navigation

sealed class Destination(val route: String){

    object SplashScreen: Destination("splash_screen")

    object LoginScreen: Destination("login_screen")

    object DevicesScreen: Destination("devices_screen")
    object PlantsScreen: Destination("plants_screen")

    object DeviceDetailAndControlScreen: Destination("device_detail_and_control_screen")
    object AddDeviceScreen: Destination("add_device_screen")

    object PlantDetailScreen: Destination("plant_detail_screen")
    object AddOrEditPlantScreen: Destination("add_or_edit_plant_screen")

    object ProfileScreen: Destination("profile_screen")
}
