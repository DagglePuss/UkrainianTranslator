package com.ukrainiantranslator.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Translate
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Translate : Screen(
        route = "translate",
        title = "Translate",
        icon = Icons.Default.Translate
    )

    data object History : Screen(
        route = "history",
        title = "History",
        icon = Icons.Default.History
    )

    companion object {
        val bottomNavItems = listOf(Translate, History)
    }
}
