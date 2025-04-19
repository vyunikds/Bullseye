package com.example.bullseye

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bullseye.screens.AboutScreen
import com.example.bullseye.screens.GameScreen
import com.example.bullseye.ui.theme.BullseyeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BullseyeTheme {
                Surface {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    var navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "gameScreen"
    ) {
        composable("gameScreen") {
            GameScreen(
                onNavigateToAboutScreen = { navController.navigate("about") }
            )
        }
        composable("about") {
            AboutScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }
    }
}


