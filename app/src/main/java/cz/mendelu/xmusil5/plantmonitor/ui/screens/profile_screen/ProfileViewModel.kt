package cz.mendelu.xmusil5.plantmonitor.ui.screens.profile_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.authentication.IAuthenticationManager
import cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.user_auth.IUserAuthRepository
import cz.mendelu.xmusil5.plantmonitor.communication.notifications.token_manager.INotificationTokenManager
import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.datastore.settings.ISettingsDataStore
import cz.mendelu.xmusil5.plantmonitor.models.api.user.PutNotificationTokenUpdate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authenticationManager: IAuthenticationManager,
    private val userAuthRepository: IUserAuthRepository,
    private val notificationTokenManager: INotificationTokenManager,
    private val settingsDataStore: ISettingsDataStore
): ViewModel() {
    val uiState: MutableState<ProfileUiState> = mutableStateOf(ProfileUiState.Start())
    val isLoading: MutableState<Boolean> = mutableStateOf(false)

    val notificationsEnabled: MutableState<Boolean> = mutableStateOf(true)

    fun loadData(){
        isLoading.value = true
        loadNotificationsEnabledState()

        val user = authenticationManager.getUser()
        if (user != null){
            uiState.value = ProfileUiState.UserLoaded(user)
        } else {
            uiState.value = ProfileUiState.Error(errorStringCode = R.string.somethingWentWrong)
        }
        isLoading.value = false
    }

    private fun loadNotificationsEnabledState(){
        isLoading.value = true
        viewModelScope.launch {
            val enabled = settingsDataStore.areNotificationsEnabled()
            notificationsEnabled.value = enabled
            isLoading.value = false
        }
    }

    fun logOut(){
        authenticationManager.logOut()
    }

    fun setNotificationsEnabled(enabled: Boolean){
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
                        is CommunicationResult.Success -> {
                            settingsDataStore.setNotificationsEnabled(true)
                            notificationsEnabled.value = true
                        }
                    }
                } else {
                    settingsDataStore.setNotificationsEnabled(false)
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
                    is CommunicationResult.Success -> {
                        settingsDataStore.setNotificationsEnabled(false)
                        notificationsEnabled.value = false
                    }
                    is CommunicationResult.Error -> {
                        uiState.value = ProfileUiState.Error(R.string.somethingWentWrong)
                    }
                    is CommunicationResult.Exception -> {
                        uiState.value = ProfileUiState.Error(R.string.connectionError)
                    }
                }
                isLoading.value = false
            }
        }
    }


}