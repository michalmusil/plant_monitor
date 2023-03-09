package com.icontio.senscare_peresonal_mobile.ui.components.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import cz.mendelu.xmusil5.plantmonitor.navigation.Destination
import cz.mendelu.xmusil5.plantmonitor.navigation.INavigationRouter

@Composable
fun ScreenSkeleton(
    navigation: INavigationRouter,
    topBar: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState()) {

    val currentBackStackEntry = navigation.getNavController().currentBackStackEntryAsState()

    // List of destinations where bottom navigation bar won't be shown
    val noBottomBarDestinations = remember{
        mutableStateListOf(
            Destination.LoginScreen.route,
            Destination.RegistrationScreen.route,
            Destination.SplashScreen.route
        )
    }

    Scaffold(
        scaffoldState = scaffoldState,
        drawerGesturesEnabled = false,
        topBar = topBar,
        bottomBar = {
            val currentDestinationRoute = currentBackStackEntry.value?.destination?.route
            currentDestinationRoute?.let {
                if (!noBottomBarDestinations.contains(it)) {
                    BottomNavBar(navigation = navigation)
                }
            }
        }
    ) {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .padding(it)
        ){
            content()
        }
    }
}