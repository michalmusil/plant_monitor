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
    val stringValidator: IStringValidator
): ViewModel() {
    val TAG = "LoginViewModel"

    val uiState: MutableState<LoginUiState> = mutableStateOf(LoginUiState.Start())

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

                    val notificationToken = notificationManager.getNotificationToken()
                    notificationToken?.let {
                        updateNotificationToken(it)
                    }

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

    suspend fun updateNotificationToken(notificationToken: String){
        val result = userAuthRepository.updateNotificationToken(
            putNotificationToken = PutNotificationTokenUpdate(
                notificationToken = notificationToken
            )
        )
        if (result !is CommunicationResult.Success){
            Log.w(TAG, "Posting FCM registration token to api failed")
        }
    }
}