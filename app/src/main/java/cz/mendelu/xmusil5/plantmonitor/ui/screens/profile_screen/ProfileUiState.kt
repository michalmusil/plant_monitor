package cz.mendelu.xmusil5.plantmonitor.ui.screens.profile_screen

sealed class ProfileUiState{
    class Start(): ProfileUiState()
    class Error(val errorStringCode: Int): ProfileUiState()
}
