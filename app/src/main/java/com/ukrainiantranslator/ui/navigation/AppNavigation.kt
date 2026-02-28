package com.ukrainiantranslator.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ukrainiantranslator.ui.screens.history.HistoryScreen
import com.ukrainiantranslator.ui.screens.translate.TranslateScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Translate.route,
        modifier = modifier
    ) {
        composable(Screen.Translate.route) {
            TranslateScreen()
        }
        composable(Screen.History.route) {
            HistoryScreen()
        }
    }
}
