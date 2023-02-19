package cz.mendelu.xmusil5.plantmonitor.ui.screens.login_screen

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.icontio.senscare_peresonal_mobile.ui.components.screens.LoadingScreen
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantmonitor.ui.components.screens.ErrorScreen
import cz.mendelu.xmusil5.plantmonitor.ui.components.ui_elements.CustomButton
import cz.mendelu.xmusil5.plantmonitor.ui.components.ui_elements.CustomTextField
import cz.mendelu.xmusil5.plantmonitor.ui.theme.errorColor

@Composable
fun LoginScreen(
    navigation: INavigationRouter,
    viewModel: LoginViewModel = hiltViewModel()
){
    val email = remember{
        mutableStateOf("")
    }

    val password = remember{
        mutableStateOf("")
    }

    viewModel.uiState.value.let {
        when(it){
            is LoginUiState.Start -> {
                LoginScreenContent(
                    navigation = navigation,
                    viewModel = viewModel,
                    email = email,
                    password = password,
                    errorMessage = null
                )
            }
            is LoginUiState.LoggingIn -> {
                LoadingScreen()
            }
            is LoginUiState.LoginSuccessfull -> {
                navigation.toPlantsScreen()
            }
            is LoginUiState.LoginFailed -> {
                LaunchedEffect(it){
                    password.value = ""
                }
                LoginScreenContent(
                    navigation = navigation,
                    viewModel = viewModel,
                    email = email,
                    password = password,
                    errorMessage = stringResource(id = it.messageStringCode)
                )
            }
            is LoginUiState.Error -> {
                LaunchedEffect(it){
                    email.value = ""
                    password.value = ""
                }
                ErrorScreen(text = stringResource(id = it.errorStringCode))
            }
        }
    }
}

@Composable
fun LoginScreenContent(
    navigation: INavigationRouter,
    viewModel: LoginViewModel,
    email: MutableState<String>,
    password: MutableState<String>,
    errorMessage: String?
){
    val localFocusManager = LocalFocusManager.current
    // FORM VALIDATION
    val userNameError = remember{
        mutableStateOf(false)
    }
    val passwordError = remember{
        mutableStateOf(false)
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            localFocusManager.clearFocus()
                        }
                    )
                }
        ) {

            CustomTextField(
                labelTitle = stringResource(id = R.string.email),
                value = email,
                maxChars = 50,
                singleLine = true,
                isError = userNameError.value,
                errorMessage = stringResource(id = R.string.emailNotInCorrectFormat),
                onTextChanged = {
                    userNameError.value = it.isBlank()
                }
            )

            CustomTextField(
                labelTitle = stringResource(id = R.string.password),
                value = password,
                maxChars = 50,
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                isError = passwordError.value,
                errorMessage = stringResource(id = R.string.passwordCantBeEmpty),
                onTextChanged = {
                    passwordError.value = it.isBlank()
                }
            )

            errorMessage?.let {
                Text(
                    text = it,
                    color = errorColor,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            CustomButton(
                text = stringResource(id = R.string.login),
                enabled = email.value.isNotBlank() && password.value.isNotBlank(),
                onClick = {
                    viewModel.uiState.value = LoginUiState.LoggingIn()
                    viewModel.login(email.value, password.value)
                }
            )
        }
    }
}