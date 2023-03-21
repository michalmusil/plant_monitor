package cz.mendelu.xmusil5.plantmonitor.ui.screens.login_screen

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.authentication.IAuthenticationManager
import cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.user_auth.IUserAuthRepository
import cz.mendelu.xmusil5.plantmonitor.communication.notifications.token_manager.INotificationTokenManager
import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.datastore.settings.ISettingsDataStore
import cz.mendelu.xmusil5.plantmonitor.datastore.user_login.IUserLoginDataStore
import cz.mendelu.xmusil5.plantmonitor.models.api.user.PostAuth
import cz.mendelu.xmusil5.plantmonitor.models.api.user.PutNotificationTokenUpdate
import cz.mendelu.xmusil5.plantmonitor.utils.validation.strings.IStringValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authenticationManager: IAuthenticationManager,
    private val userAuthRepository: IUserAuthRepository,
    private val notificationManager: INotificationTokenManager,
    private val settingsDataStore: ISettingsDataStore,
    private val userLoginDataStore: IUserLoginDataStore,
    val stringValidator: IStringValidator
): ViewModel() {
    val TAG = "LoginViewModel"

    val uiState: MutableState<LoginUiState> = mutableStateOf(LoginUiState.Start())

    fun attemptToRestoreSignedUser(){
        viewModelScope.launch {
            val lastLoggedIn = userLoginDataStore.getSavedUserLogin()
            if (lastLoggedIn != null){
                authenticationManager.setUser(lastLoggedIn)
                val check = userAuthRepository.checkCurrentSignedUserValid()
                when(check){
                    is CommunicationResult.Success -> {
                        uiState.value = LoginUiState.LoginSuccessfull(user = lastLoggedIn)
                    }
                    is CommunicationResult.Exception -> {
                        uiState.value = LoginUiState.Error(errorStringCode = R.string.connectionError)
                    }
                    else -> {
                        authenticationManager.setUser(null)
                        uiState.value = LoginUiState.ProceedWithLogin()
                    }
                }
            } else {
                uiState.value = LoginUiState.ProceedWithLogin()
            }
        }
    }

    fun login(email: String, password: String){
        uiState.value = LoginUiState.LoggingIn()
        viewModelScope.launch {
            val userAuth = PostAuth(
                email = email,
                password = password
            )
            val result = userAuthRepository.login(userAuth)
            when(result){
                is CommunicationResult.Success -> {
                    authenticationManager.setUser(user = result.data)
                    if (settingsDataStore.areNotificationsEnabled(user = result.data)){
                        updateNotificationToken()
                    }
                    userLoginDataStore.saveUserLogin(result.data)

                    uiState.value = LoginUiState.LoginSuccessfull(user = result.data)
                }
                is CommunicationResult.Exception -> {
                    uiState.value = LoginUiState.Error(errorStringCode = R.string.connectionError)
                }
                is CommunicationResult.Error -> {
                    uiState.value = LoginUiState.LoginFailed(messageStringCode = R.string.invalidCredentials)
                }
            }
        }
    }

    fun emailAndPasswordAreValid(email: String, password: String): Boolean{
        return stringValidator.isEmailValid(email) &&
                stringValidator.isPasswordValid(password)
    }

    suspend fun updateNotificationToken(){
        val notificationToken = notificationManager.getNotificationToken()
        notificationToken?.let {
            val result = userAuthRepository.updateNotificationToken(
                putNotificationToken = PutNotificationTokenUpdate(
                    notificationToken = notificationToken
                )
            )
            if (result !is CommunicationResult.Success) {
                Log.w(TAG, "Posting FCM registration token to api failed")
            }
        }
    }
}