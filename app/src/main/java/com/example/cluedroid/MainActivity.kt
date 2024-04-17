package com.example.cluedroid

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.integerResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cluedroid.db.TemplateRoomDatabase
import com.example.cluedroid.model.Theme
import com.example.cluedroid.repository.ActiveTemplateRepository
import com.example.cluedroid.repository.UserSettingsRepository
import com.example.cluedroid.ui.windows.cluedroidGame.CluedroidGame
import com.example.cluedroid.ui.windows.settings.Settings
import com.example.cluedroid.ui.theme.CluedroidTheme
import com.example.cluedroid.ui.windows.chooseTemplate.ChooseTemplate
import com.example.cluedroid.ui.windows.startGame.StartGame
import com.example.cluedroid.ui.windows.templateEditor.TemplateEditorCreateMode
import com.example.cluedroid.ui.windows.templateEditor.TemplateEditorEditMode
import com.example.cluedroid.view.ActiveTemplateViewModel
import com.example.cluedroid.view.UserSettingsViewModel

class MainActivity : ComponentActivity() {
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val userSettingsViewModel = UserSettingsViewModel(
                UserSettingsRepository(
                    TemplateRoomDatabase.getInstance(LocalContext.current).userSettingsDao()
                )
            )
            val activeTemplateViewModel = ActiveTemplateViewModel(
                ActiveTemplateRepository(
                    TemplateRoomDatabase.getInstance(LocalContext.current).activeTemplateDao()
                )
            )

            AppCompatDelegate.setDefaultNightMode(
                when (userSettingsViewModel.getTheme()) {
                    Theme.LIGHT.toString() -> AppCompatDelegate.MODE_NIGHT_NO
                    Theme.DARK.toString() -> AppCompatDelegate.MODE_NIGHT_YES
                    Theme.AUTO.toString() -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }
            )

            val navController = rememberNavController()
            val transitionDurationMillis =
                integerResource(id = R.integer.transition_duration_millis)
            var editModeTemplateId by remember {
                mutableIntStateOf(0)
            }

            CluedroidTheme {
                NavHost(
                    navController = navController,
                    startDestination =
                    if (activeTemplateViewModel.getActiveTemplateData().gameStarted.toBoolean())
                        Route.cluedroidGame
                    else
                        Route.startGame
                ) {
                    composable(
                        route = Route.cluedroidGame,
                        enterTransition = {
                            return@composable slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Right,
                                tween(transitionDurationMillis)
                            )
                        },
                        exitTransition = {
                            return@composable slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Left,
                                tween(transitionDurationMillis)
                            )
                        }
                    ) {
                        CluedroidGame(
                            navigateToSettings = {
                                navController.navigate(Route.settings)
                            },
                            navigateToStartGame = {
                                navController.navigate(Route.startGame)
                            }
                        )
                    }
                    composable(
                        route = Route.settings,
                        enterTransition = {
                            return@composable slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Left,
                                tween(transitionDurationMillis)
                            )
                        },
                        exitTransition = {
                            return@composable slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Right,
                                tween(transitionDurationMillis)
                            )
                        }
                    ) {
                        Settings(
                            navigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                    composable(
                        route = Route.startGame,
                        enterTransition = {
                            val currentRoute =
                                navController.previousBackStackEntry?.destination?.route
                            return@composable slideIntoContainer(
                                when (currentRoute) {
                                    Route.cluedroidGame -> AnimatedContentTransitionScope.SlideDirection.Right
                                    Route.chooseTemplate, Route.templateCreator, Route.templateEditor -> AnimatedContentTransitionScope.SlideDirection.Left
                                    else -> AnimatedContentTransitionScope.SlideDirection.Right
                                },
                                tween(transitionDurationMillis)
                            )
                        },
                        exitTransition = {
                            val currentRoute =
                                navController.currentBackStackEntry?.destination?.route
                            return@composable slideOutOfContainer(
                                when (currentRoute) {
                                    Route.chooseTemplate -> AnimatedContentTransitionScope.SlideDirection.Right
                                    else -> AnimatedContentTransitionScope.SlideDirection.Left
                                },
                                tween(transitionDurationMillis)
                            )
                        },
                        popEnterTransition = {
                            return@composable slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Right,
                                tween(transitionDurationMillis)
                            )
                        }
                    ) {
                        StartGame(
                            navigateToCluedroidGame = {
                                navController.navigate(Route.cluedroidGame)
                            },
                            navigateToSettings = {
                                navController.navigate(Route.settings)
                            },
                            navigateToChooseTemplate = {
                                navController.navigate(Route.chooseTemplate)
                            }
                        )
                    }
                    composable(
                        route = Route.chooseTemplate,
                        enterTransition = {
                            return@composable slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Right,
                                tween(transitionDurationMillis)
                            )
                        },
                        popEnterTransition = null,
                        exitTransition = {
                            val currentRoute =
                                navController.currentBackStackEntry?.destination?.route
                            if (currentRoute == Route.templateEditor || currentRoute == Route.templateCreator) {
                                null
                            } else {
                                return@composable slideOutOfContainer(
                                    AnimatedContentTransitionScope.SlideDirection.Left,
                                    tween(transitionDurationMillis)
                                )
                            }
                        }
                    ) {
                        ChooseTemplate(
                            navigateBack = {
                                navController.navigate(Route.startGame)
                            },
                            navigateToSettings = {
                                navController.navigate(Route.settings)
                            },
                            navigateToEditTemplate = {
                                navController.navigate(Route.templateEditor)
                            },
                            navigateToCreateTemplate = {
                                navController.navigate(Route.templateCreator)
                            },
                            updateEditSelectedTemplateId = {
                                editModeTemplateId = it
                            }
                        )
                    }
                    composable(
                        route = Route.templateEditor,
                        enterTransition = {
                            return@composable slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Up,
                                tween(transitionDurationMillis)
                            )
                        },
                        exitTransition = {
                            val currentRoute =
                                navController.currentBackStackEntry?.destination?.route
                            return@composable slideOutOfContainer(
                                when (currentRoute) {
                                    Route.startGame -> AnimatedContentTransitionScope.SlideDirection.Left
                                    else -> AnimatedContentTransitionScope.SlideDirection.Down
                                },
                                tween(transitionDurationMillis)
                            )
                        }
                    ) {
                        TemplateEditorEditMode(
                            editModeTemplateId = editModeTemplateId,
                            navigateBack = {
                                navController.popBackStack()
                            },
                            navigateToStartGame = {
                                navController.navigate(Route.startGame)
                            }
                        )
                    }
                    composable(
                        route = Route.templateCreator,
                        enterTransition = {
                            return@composable slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Up,
                                tween(transitionDurationMillis)
                            )
                        },
                        exitTransition = {
                            val currentRoute =
                                navController.currentBackStackEntry?.destination?.route
                            return@composable slideOutOfContainer(
                                when (currentRoute) {
                                    Route.startGame -> AnimatedContentTransitionScope.SlideDirection.Left
                                    else -> AnimatedContentTransitionScope.SlideDirection.Down
                                },
                                tween(transitionDurationMillis)
                            )
                        }
                    ) {
                        TemplateEditorCreateMode(
                            navigateBack = {
                                navController.popBackStack()
                            },
                            navigateToStartGame = {
                                navController.navigate(Route.startGame)
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
        const val chooseTemplate = "ChooseTemplate"
        const val templateEditor = "TemplateEditor"
        const val templateCreator = "TemplateCreator"
    }
}
