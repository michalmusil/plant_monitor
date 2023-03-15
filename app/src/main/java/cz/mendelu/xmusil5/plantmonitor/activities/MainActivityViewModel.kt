package cz.mendelu.xmusil5.plantmonitor.activities

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.xmusil5.plantmonitor.authentication.IAuthenticationManager
import cz.mendelu.xmusil5.plantmonitor.datastore.settings.ISettingsDataStore
import cz.mendelu.xmusil5.plantmonitor.navigation.INavigationRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    val authenticationManager: IAuthenticationManager,
    val navigationRouter: INavigationRouter,
    private val settingsDataStore: ISettingsDataStore
): ViewModel() {
    val darkModePreference: MutableState<Boolean?> = mutableStateOf(null)

    fun subscribeToDarkModeChanges(){
        viewModelScope.launch {
            settingsDataStore.darkModePreference.collectLatest {
                darkModePreference.value = it
            }
        }
    }
}