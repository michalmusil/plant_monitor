package cz.mendelu.xmusil5.plantmonitor.ui.screens.profile_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.authentication.IAuthenticationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authenticationManager: IAuthenticationManager
): ViewModel() {
    val uiState: MutableState<ProfileUiState> = mutableStateOf(ProfileUiState.Start())

    fun loadUser(){
        val user = authenticationManager.getUser()
        if (user != null){
            uiState.value = ProfileUiState.UserLoaded(user)
        } else {
            uiState.value = ProfileUiState.Error(errorStringCode = R.string.somethingWentWrong)
        }
    }

    fun logOut(){
        authenticationManager.logOut()
    }


}