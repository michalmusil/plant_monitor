package cz.mendelu.xmusil5.plantmonitor.ui.screens.registration_screen

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.authentication.IAuthenticationManager
import cz.mendelu.xmusil5.plantmonitor.communication.api.repositories.user_auth.IUserAuthRepository
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
class RegistrationViewModel @Inject constructor(
    private val authenticationManager: IAuthenticationManager,
    private val userAuthRepository: IUserAuthRepository,
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
                is CommunicationResult.Success -> {
                    login(registeredAuth = auth)
                }
                is CommunicationResult.Error -> {
                    uiState.value = RegistrationUiState.RegistrationFailed(messageStringCode = R.string.somethingWentWrong)
                }
                is CommunicationResult.Exception -> {
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
                is CommunicationResult.Success -> {
                    authenticationManager.setUser(user = result.data)

                    val notificationToken = getNotificationToken()
                    notificationToken?.let {
                        updateNotificationToken(it)
                    }

                    uiState.value = RegistrationUiState.RegistrationAndLoginSuccessfull(user = result.data)
                }
                is CommunicationResult.Exception -> {
                    uiState.value = RegistrationUiState.Error(errorStringCode = R.string.connectionError)
                }
                is CommunicationResult.Error -> {
                    uiState.value = RegistrationUiState.LoginFailed()
                }
            }
        }
    }

    fun emailAndPasswordAreValid(email: String, password: String): Boolean{
        return stringValidator.isEmailValid(email) &&
                stringValidator.isPasswordValid(password)
    }

    suspend fun getNotificationToken(): String?{
        val token = suspendCoroutine<String?>{ continuation ->
            FirebaseMessaging.getInstance().token.addOnCompleteListener{ task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                    continuation.resume(null)
                }
                else {
                    val token = task.result
                    continuation.resume(token)
                }
            }
        }
        return token
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