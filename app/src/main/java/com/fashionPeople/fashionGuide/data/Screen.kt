package com.fashionPeople.fashionGuide.data

import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings

import androidx.compose.ui.graphics.vector.ImageVector


sealed class Screen(val route: String, val icon: ImageVector) {
    data object Home : Screen("옷 추천", Icons.Default.Home)
    data object Closet : Screen("옷장", Icons.Default.List)
    data object Settings : Screen("설정", Icons.Default.Settings)

}
