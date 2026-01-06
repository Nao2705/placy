package com.example.placy

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

// Определяем маршруты навигации
sealed class Screen(val route: String) {
    object Map : Screen("map")
    object Camera : Screen("camera")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            AppBottomNavigation(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Map.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(Screen.Map.route) {
                TempMapScreen()
            }

            composable(Screen.Camera.route) {
                CameraView(navController = navController, 15.4, 12.1) // Передаем NavController
            }
        }
    }
}

@Composable
fun AppBottomNavigation(navController: NavController) {
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route

    NavigationBar {
        // Кнопка "Карта"
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Map,
                    contentDescription = "Карта"
                )
            },
            label = { Text("Карта") },
            selected = currentRoute == Screen.Map.route,
            onClick = {
                if (currentRoute != Screen.Map.route) {
                    navController.navigate(Screen.Map.route) {
                        // Очищаем стек навигации до корня при переходе к карте
                        popUpTo(Screen.Map.route) {
                            inclusive = true
                        }
                    }
                }
            }
        )

        // Кнопка "Камера"
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Камера"
                )
            },
            label = { Text("Камера") },
            selected = currentRoute == Screen.Camera.route,
            onClick = {
                if (currentRoute != Screen.Camera.route) {
                    navController.navigate(Screen.Camera.route) {
                        // Очищаем стек навигации до корня при переходе к камере
                        popUpTo(Screen.Map.route) {
                            inclusive = false
                        }
                    }
                }
            }
        )
    }
}