package cz.mendelu.xmusil5.plantmonitor.ui.screens.login_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.authentication.IAuthenticationManager
import cz.mendelu.xmusil5.plantmonitor.communication.repositories.user_auth.IUserAuthRepository
import cz.mendelu.xmusil5.plantmonitor.communication.utils.CommunicationResult
import cz.mendelu.xmusil5.plantmonitor.models.api.user.PostAuth
import cz.mendelu.xmusil5.plantmonitor.utils.validation.IStringValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authenticationManager: IAuthenticationManager,
    private val userAuthRepository: IUserAuthRepository,
    val stringValidator: IStringValidator
): ViewModel() {

    val uiState: MutableState<LoginUiState> = mutableStateOf(LoginUiState.Start())

    fun login(email: String, password: String){
        viewModelScope.launch {
            val userAuth = PostAuth(
                email = email,
                password = password
            )
            val result = userAuthRepository.login(userAuth)
            when(result){
                is CommunicationResult.Success -> {
                    authenticationManager.setUser(user = result.data)
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
}