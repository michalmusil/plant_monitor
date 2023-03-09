package cz.mendelu.xmusil5.plantmonitor.ui.screens.registration_screen

import cz.mendelu.xmusil5.plantmonitor.models.api.user.GetUser
import cz.mendelu.xmusil5.plantmonitor.ui.screens.login_screen.LoginUiState

sealed class RegistrationUiState{
    class Start(): RegistrationUiState()
    class RegistrationInProcess(): RegistrationUiState()
    class RegistrationAndLoginSuccessfull(val user: GetUser): RegistrationUiState()
    class RegistrationFailed(val messageStringCode: Int): RegistrationUiState()
    class LoginFailed(): RegistrationUiState()
    class Error(val errorStringCode: Int): RegistrationUiState()
}
