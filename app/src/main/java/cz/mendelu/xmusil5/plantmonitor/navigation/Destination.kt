package cz.mendelu.xmusil5.plantmonitor.navigation

sealed class Destination(val route: String){

    object LoginScreen: Destination("login_screen")

    object DevicesScreen: Destination("devices_screen")
    object PlantsScreen: Destination("plants_screen")

    object DeviceDetailScreen: Destination("device_detail_screen")
    object PlantDetailScreen: Destination("plant_detail_screen")

    object AddDeviceScreen: Destination("add_device_screen")

    object AddOrEditPlantScreen: Destination("add_or_edit_plant_screen")

    object ProfileScreen: Destination("profile_screen")
}
