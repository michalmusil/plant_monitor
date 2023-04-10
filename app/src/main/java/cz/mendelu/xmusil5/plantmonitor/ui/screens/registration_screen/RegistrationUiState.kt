package cz.mendelu.xmusil5.plantmonitor.ui.screens.registration_screen

import cz.mendelu.xmusil5.plantmonitor.models.api.user.User

sealed class RegistrationUiState{
    class Start(): RegistrationUiState()
    class RegistrationInProcess(): RegistrationUiState()
    class RegistrationAndLoginSuccessfull(val user: User): RegistrationUiState()
    class RegistrationFailed(val messageStringCode: Int): RegistrationUiState()
    class LoginFailed(): RegistrationUiState()
    class Error(val errorStringCode: Int): RegistrationUiState()
}
