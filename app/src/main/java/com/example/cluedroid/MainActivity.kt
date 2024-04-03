package com.example.cluedroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cluedroid.db.TemplateRoomDatabase
import com.example.cluedroid.model.Theme
import com.example.cluedroid.repository.UserSettingsRepository
import com.example.cluedroid.ui.windows.cluedroidGame.CluedroidGame
import com.example.cluedroid.ui.windows.settings.Settings
import com.example.cluedroid.ui.theme.CluedroidTheme
import com.example.cluedroid.ui.windows.startGame.StartGame
import com.example.cluedroid.view.UserSettingsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val userSettingsViewModel = UserSettingsViewModel(
                UserSettingsRepository(
                    TemplateRoomDatabase.getInstance(LocalContext.current).userSettingsDao()
                )
            )

            AppCompatDelegate.setDefaultNightMode(
                when(userSettingsViewModel.getTheme()) {
                    Theme.LIGHT.toString() -> AppCompatDelegate.MODE_NIGHT_NO
                    Theme.DARK.toString() -> AppCompatDelegate.MODE_NIGHT_YES
                    Theme.AUTO.toString() ->  AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }
            )

            val navController = rememberNavController()

            CluedroidTheme {
                NavHost(navController = navController, startDestination = Route.cluedroidGame) {
                    composable(route = Route.cluedroidGame) {
                        CluedroidGame (
                            navigateToSettings = {
                                navController.navigate(Route.settings)
                            },
                            navigateToStartGame = {
                                navController.navigate(Route.startGame)
                            }
                        )
                    }
                    composable(route = Route.settings) {
                        Settings(
                            navigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                    composable(route = Route.startGame) {
                        StartGame(
                            navigateToCluedroidGame = {
                                navController.navigate(Route.cluedroidGame)
                            },
                            navigateToSettings = {
                                navController.navigate(Route.settings)
                            },
                            navigateToSelectTemplate = {
                                //navController.navigate(Route.)
                            }
                        )
                    }
                }
            }
        }
    }

    object Route {
        const val cluedroidGame = "CluedroidGame"
        const val settings = "Settings"
        const val startGame = "StartGame"
    }
}
