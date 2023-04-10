package cz.mendelu.xmusil5.plantmonitor.ui.screens.profile_screen

import cz.mendelu.xmusil5.plantmonitor.models.api.user.User

sealed class ProfileUiState{
    class Start(): ProfileUiState()
    class UserLoaded(val user: User): ProfileUiState()
    class Error(val errorStringCode: Int): ProfileUiState()
}
