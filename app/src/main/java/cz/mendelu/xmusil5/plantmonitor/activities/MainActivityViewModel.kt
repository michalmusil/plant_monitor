package cz.mendelu.xmusil5.plantmonitor.activities

import androidx.lifecycle.ViewModel
import cz.mendelu.xmusil5.plantmonitor.authentication.IAuthenticationManager
import cz.mendelu.xmusil5.plantmonitor.navigation.INavigationRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    val authenticationManager: IAuthenticationManager,
    val navigationRouter: INavigationRouter
): ViewModel() {

}