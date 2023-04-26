package cz.mendelu.xmusil5.plantmonitor.ui.screens.profile_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.user_session.IUserSessionManager
import cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.user_auth.IUserAuthRepository
import cz.mendelu.xmusil5.plantmonitor.communication.notifications.token_manager.INotificationTokenManager
import cz.mendelu.xmusil5.plantmonitor.communication.utils.DataResult
import cz.mendelu.xmusil5.plantmonitor.datastore.settings.ISettingsDataStore
import cz.mendelu.xmusil5.plantmonitor.models.api.user.User
import cz.mendelu.xmusil5.plantmonitor.models.api.user.PutNotificationTokenUpdate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userSessionManager: IUserSessionManager,
    private val userAuthRepository: IUserAuthRepository,
    private val notificationTokenManager: INotificationTokenManager,
    private val settingsDataStore: ISettingsDataStore
): ViewModel() {
    val uiState: MutableState<ProfileUiState> = mutableStateOf(ProfileUiState.Start())
    val isLoading: MutableState<Boolean> = mutableStateOf(false)
    val darkModePreference: MutableState<Boolean?> = mutableStateOf(null)

    val notificationsEnabled: MutableState<Boolean> = mutableStateOf(true)

    fun loadData(){
        isLoading.value = true
        subscribeToDarkModeChanges()

        viewModelScope.launch {
            val user = userSessionManager.getUser()
            if (user != null) {
                uiState.value = ProfileUiState.UserLoaded(user)
                loadNotificationsEnabledState(user = user)
            } else {
                uiState.value = ProfileUiState.Error(errorStringCode = R.string.somethingWentWrong)
            }
            isLoading.value = false
        }
    }

    private fun loadNotificationsEnabledState(user: User){
        isLoading.value = true
        viewModelScope.launch {
            val enabled = settingsDataStore.areNotificationsEnabled(user = user)
            notificationsEnabled.value = enabled
            isLoading.value = false
        }
    }

    fun logOut(){
        userSessionManager.logOut()
    }

    fun setNotificationsEnabled(enabled: Boolean, user: User){
        isLoading.value = true
        if (enabled){
            viewModelScope.launch {
                val newToken = notificationTokenManager.getNotificationToken()
                if (newToken != null){
                    val result = userAuthRepository.updateNotificationToken(
                        PutNotificationTokenUpdate(
                            notificationToken = newToken
                        )
                    )
                    when(result){
                        is DataResult.Success -> {
                            settingsDataStore.setNotificationsEnabled(true, user)
                            notificationsEnabled.value = true
                        }
                    }
                } else {
                    settingsDataStore.setNotificationsEnabled(false, user)
                    notificationsEnabled.value = false
                }

                isLoading.value = false
            }
        } else {
            viewModelScope.launch {
                val result = userAuthRepository.updateNotificationToken(
                    putNotificationToken = PutNotificationTokenUpdate(
                        notificationToken = null
                    )
                )
                when(result){
                    is DataResult.Success -> {
                        settingsDataStore.setNotificationsEnabled(false, user)
                        notificationsEnabled.value = false
                    }
                    is DataResult.Error -> {
                        uiState.value = ProfileUiState.Error(R.string.somethingWentWrong)
                    }
                    is DataResult.Exception -> {
                        uiState.value = ProfileUiState.Error(R.string.connectionError)
                    }
                }
                isLoading.value = false
            }
        }
    }


    fun overrideDarkModePreference(darkMode: Boolean?){
        isLoading.value = true
        viewModelScope.launch {
            settingsDataStore.setDarkModePreference(darkMode)
            isLoading.value = false
        }
    }

    private fun subscribeToDarkModeChanges(){
        viewModelScope.launch {
            settingsDataStore.darkModePreference.collectLatest {
                darkModePreference.value = it
            }
        }
    }
}