package cz.mendelu.xmusil5.plantmonitor.ui.screens.login_screen

import cz.mendelu.xmusil5.plantmonitor.models.api.user.User

sealed class LoginUiState{
    class Start(): LoginUiState()
    class ProceedWithLogin(): LoginUiState()
    class LoggingIn(): LoginUiState()
    class LoginSuccessfull(val user: User): LoginUiState()
    class LoginFailed(val messageStringCode: Int): LoginUiState()
    class Error(val errorStringCode: Int): LoginUiState()
}