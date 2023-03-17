package cz.mendelu.xmusil5.plantmonitor.navigation

import androidx.navigation.NavController

interface INavigationRouter {
    fun getNavController(): NavController
    fun setNavController(navController: NavController)
    fun returnBack()

    fun toLogin()
    fun toRegistration()

    fun toPlantsScreen()
    fun toDevicesScreen()
    fun toProfileScreen()

    fun toPlantDetail(plantId: Long)
    fun toDeviceDetailAndControl(deviceId: Long)

    fun toAddPlant()
    fun toEditPlant(plantId: Long)
}