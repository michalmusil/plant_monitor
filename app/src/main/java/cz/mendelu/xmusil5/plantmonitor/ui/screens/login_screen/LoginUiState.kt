package cz.mendelu.xmusil5.plantmonitor.ui.screens.login_screen

sealed class LoginUiState{
    class Start(): LoginUiState()
    class Error(val errorStringCode: Int): LoginUiState()
}