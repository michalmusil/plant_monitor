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

    fun toPlantDetail(plantId: Long)
    fun toDeviceDetail(deviceId: Long)

    fun toAddPlant()
    fun toAddDevice()
}