package cz.mendelu.xmusil5.plantmonitor.navigation

import androidx.navigation.NavController

interface INavigationRouter {
    fun getNavController(): NavController
    fun setNavController(navController: NavController)
    fun returnBack()

    fun toLogin()

    fun toPlantsScreen()
    fun toDevicesScreen()
    fun toProfileScreen()

    fun toPlantDetail(plantId: Int)
    fun toDeviceDetail(deviceId: Int)

    fun toAddPlant()
    fun toAddDevice()
}