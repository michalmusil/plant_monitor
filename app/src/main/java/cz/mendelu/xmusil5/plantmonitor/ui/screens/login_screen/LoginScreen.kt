package cz.mendelu.xmusil5.plantmonitor.ui.screens.login_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val email = rememberSaveable{
        mutableStateOf("")
    }

    val password = rememberSaveable{
        mutableStateOf("")
    }

    viewModel.uiState.value.let {
        when(it){
            is LoginUiState.Start -> {
                LoadingScreen()
                LaunchedEffect(it){
                    viewModel.attemptToRestoreSignedUser()
                }
            }
            is LoginUiState.ProceedWithLogin -> {
                LoginScreenContent(
                    navigation = navigation,
                    viewModel = viewModel,
                    email = email,
                    password = password,
                )
            }
            is LoginUiState.LoggingIn -> {
                LoadingScreen()
            }
            is LoginUiState.LoginSuccessfull -> {
                LaunchedEffect(it){
                    navigation.toPlantsScreen()
                }
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
                ErrorScreen(text = stringResource(id = it.errorStringCode)){
                    viewModel.uiState.value = LoginUiState.Start()
                }
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
    errorMessage: String? = null
){
    val localFocusManager = LocalFocusManager.current
    // FORM VALIDATION
    val emailError = remember{
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
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        localFocusManager.clearFocus()
                    }
                )
            }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
        ) {
            
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.app_logo), 
                contentDescription = stringResource(id = R.string.appLogo),
                modifier = Modifier
                    .size(220.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                localFocusManager.clearFocus()
                            }
                        )
                    }
            )

            Text(
                text = stringResource(id = R.string.login),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(vertical = 12.dp)
            )

            CustomTextField(
                labelTitle = stringResource(id = R.string.email),
                value = email,
                maxChars = 50,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    autoCorrect = false,
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                isError = emailError.value,
                errorMessage = stringResource(id = R.string.emailNotInCorrectFormat),
                onTextChanged = {
                    emailError.value = !viewModel.stringValidator.isEmailValid(it)
                }
            )

            CustomTextField(
                labelTitle = stringResource(id = R.string.password),
                value = password,
                maxChars = 50,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    autoCorrect = false,
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                visualTransformation = PasswordVisualTransformation(),
                isError = passwordError.value,
                errorMessage = stringResource(id = R.string.passwordTooShort),
                onTextChanged = {
                    passwordError.value = !viewModel.stringValidator.isPasswordValid(it)
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            errorMessage?.let {
                Text(
                    text = it,
                    color = errorColor,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            CustomButton(
                text = stringResource(id = R.string.login),
                enabled = viewModel.emailAndPasswordAreValid(
                    email = email.value,
                    password = password.value
                ),
                onClick = {
                    viewModel.login(email.value, password.value)
                }
            )

            Spacer(modifier = Modifier.height(25.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.noAccountYet),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                
                Spacer(modifier = Modifier.width(6.dp))

                CustomButton(
                    text = stringResource(id = R.string.goToRegister),
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    textColor = MaterialTheme.colorScheme.onSurface,
                    textSize = 12.sp,
                    onClick = {
                        navigation.toRegistration()
                    }
                )
            }
        }
    }
}