package cz.mendelu.xmusil5.plantmonitor.ui.screens.profile_screen

import cz.mendelu.xmusil5.plantmonitor.models.api.user.GetUser

sealed class ProfileUiState{
    class Start(): ProfileUiState()
    class UserLoaded(val user: GetUser): ProfileUiState()
    class Error(val errorStringCode: Int): ProfileUiState()
}
