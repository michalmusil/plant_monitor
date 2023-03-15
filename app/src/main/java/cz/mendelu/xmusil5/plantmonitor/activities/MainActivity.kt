package cz.mendelu.xmusil5.plantmonitor.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import cz.mendelu.xmusil5.plantmonitor.authentication.IAuthenticationManager
import cz.mendelu.xmusil5.plantmonitor.navigation.Destination
import cz.mendelu.xmusil5.plantmonitor.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantmonitor.navigation.NavGraph
import cz.mendelu.xmusil5.plantmonitor.ui.theme.PlantMonitorTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LaunchedEffect(this){
                viewModel.subscribeToDarkModeChanges()
            }

            val darkModeSystemDefault = isSystemInDarkTheme()
            val darkMode = remember{
                mutableStateOf(darkModeSystemDefault)
            }
            LaunchedEffect(viewModel.darkModePreference.value){
                if (viewModel.darkModePreference.value != null){
                    darkMode.value = viewModel.darkModePreference.value!!
                } else {
                    darkMode.value = darkModeSystemDefault
                }
            }

            PlantMonitorTheme(
                darkTheme = darkMode.value
            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph(
                        authenticationManager = viewModel.authenticationManager,
                        navigation = viewModel.navigationRouter,
                        startDestination = Destination.SplashScreen.route
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PlantMonitorTheme {
        Greeting("Android")
    }
}