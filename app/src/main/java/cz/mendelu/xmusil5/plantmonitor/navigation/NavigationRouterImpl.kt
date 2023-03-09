package cz.mendelu.xmusil5.plantmonitor.navigation

import androidx.navigation.NavController

class NavigationRouterImpl: INavigationRouter {

    private lateinit var navController: NavController

    private fun emptyBackstackAndNavigate(destination: Destination){
        /*
        navController.navigate(destination.route){
            popUpTo(destination.route){
                inclusive = true
                saveState = false
            }
        }
         */
        navController.backQueue.clear()
        navController.navigate(destination.route)
    }

    override fun setNavController(navController: NavController){
        this.navController = navController
    }

    override fun getNavController(): NavController {
        return this.navController
    }

    override fun returnBack() {
        navController.popBackStack()
    }


    override fun toLogin() {
        emptyBackstackAndNavigate(Destination.LoginScreen)
    }

    override fun toRegistration() {
        emptyBackstackAndNavigate(Destination.RegistrationScreen)
    }

    override fun toPlantsScreen() {
        emptyBackstackAndNavigate(Destination.PlantsScreen)
    }

    override fun toDevicesScreen() {
        emptyBackstackAndNavigate(Destination.DevicesScreen)
    }

    override fun toProfileScreen() {
        emptyBackstackAndNavigate(Destination.ProfileScreen)
    }




    override fun toPlantDetail(plantId: Long) {
        navController.navigate("${Destination.PlantDetailScreen.route}/${plantId}")
    }

    override fun toDeviceDetailAndControl(deviceId: Long) {
        navController.navigate("${Destination.DeviceDetailAndControlScreen.route}/${deviceId}")
    }

    override fun toAddPlant() {
        navController.navigate("${Destination.AddOrEditPlantScreen.route}/-1L")
    }

    override fun toEditPlant(plantId: Long) {
        navController.navigate("${Destination.AddOrEditPlantScreen.route}/${plantId}")
    }

    override fun toAddDevice() {
        navController.navigate(Destination.AddDeviceScreen.route)
    }
}