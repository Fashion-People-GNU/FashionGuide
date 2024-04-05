package com.fashionPeople.fashionGuide.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val icon: ImageVector) {
    data object Home : Screen("home", Icons.Default.Home)
    data object Favorites : Screen("favorites", Icons.Default.Favorite)
    data object Settings : Screen("settings", Icons.Default.Settings)
}
