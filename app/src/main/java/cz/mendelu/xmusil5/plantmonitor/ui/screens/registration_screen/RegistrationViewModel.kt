package cz.mendelu.xmusil5.plantmonitor.ui.screens.registration_screen

import android.util.Log
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
import cz.mendelu.xmusil5.plantmonitor.models.api.user.PostAuth
import cz.mendelu.xmusil5.plantmonitor.models.api.user.PutNotificationTokenUpdate
import cz.mendelu.xmusil5.plantmonitor.utils.validation.strings.IStringValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val userSessionManager: IUserSessionManager,
    private val userAuthRepository: IUserAuthRepository,
    private val notificationTokenManager: INotificationTokenManager,
    private val settingsDataStore: ISettingsDataStore,
    val stringValidator: IStringValidator
): ViewModel() {
    val TAG = "RegistrationViewModel"

    val uiState: MutableState<RegistrationUiState> = mutableStateOf(RegistrationUiState.Start())

    fun register(email: String, password: String){
        uiState.value = RegistrationUiState.RegistrationInProcess()
        viewModelScope.launch {
            val auth = PostAuth(
                email = email,
                password = password
            )
            val result = userAuthRepository.register(auth)
            when(result){
                is DataResult.Success -> {
                    login(registeredAuth = auth)
                }
                is DataResult.Error -> {
                    uiState.value = RegistrationUiState.RegistrationFailed(messageStringCode = R.string.somethingWentWrong)
                }
                is DataResult.Exception -> {
                    uiState.value = RegistrationUiState.Error(errorStringCode = R.string.connectionError)
                }
            }
        }
    }

    private fun login(registeredAuth: PostAuth){
        viewModelScope.launch {
            val userAuth = PostAuth(
                email = registeredAuth.email,
                password = registeredAuth.password
            )
            val result = userAuthRepository.login(userAuth)
            when(result){
                is DataResult.Success -> {
                    userSessionManager.setUser(user = result.data)

                    if (settingsDataStore.areNotificationsEnabled(user = result.data)){
                        updateNotificationToken()
                    }

                    uiState.value = RegistrationUiState.RegistrationAndLoginSuccessfull(user = result.data)
                }
                is DataResult.Exception -> {
                    uiState.value = RegistrationUiState.Error(errorStringCode = R.string.connectionError)
                }
                is DataResult.Error -> {
                    uiState.value = RegistrationUiState.LoginFailed()
                }
            }
        }
    }

    fun emailAndPasswordAreValid(email: String, password: String): Boolean{
        return stringValidator.isEmailValid(email) &&
                stringValidator.isPasswordValid(password)
    }

    suspend fun updateNotificationToken(){
        val notificationToken = notificationTokenManager.getNotificationToken()
        notificationToken?.let {
            val result = userAuthRepository.updateNotificationToken(
                putNotificationToken = PutNotificationTokenUpdate(
                    notificationToken = notificationToken
                )
            )
            if (result !is DataResult.Success) {
                Log.w(TAG, "Posting FCM registration token to api failed")
            }
        }
    }
}