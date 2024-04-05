package com.fashionPeople.fashionGuide.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fashionPeople.fashionGuide.data.Screen

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) { innerPaddingModifier ->
// Inner padding from the bottom bar
        NavHost(
            navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPaddingModifier)
        ) {
            composable(Screen.Home.route) {

                HomeScreen()

            }
            composable(Screen.Favorites.route) {

                FavoritesScreen()
            }
            composable(Screen.Search.route) {

                SearchScreen()

            }
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    NavigationBar {
        val currentDestination = navController.currentDestination?.route
        NavigationBarItem(
            icon = { Icon(Screen.Home.icon, contentDescription = null) },
            label = { Text("Home") },
            selected = currentDestination == Screen.Home.route,
            onClick = { navController.navigate(Screen.Home.route) }
        )
        NavigationBarItem(
            icon = { Icon(Screen.Favorites.icon, contentDescription = null) },
            label = { Text("Favorites") },
            selected = currentDestination == Screen.Favorites.route,
            onClick = { navController.navigate(Screen.Favorites.route) }
        )
        NavigationBarItem(
            icon = { Icon(Screen.Search.icon, contentDescription = null) },
            label = { Text("Search") },
            selected = currentDestination == Screen.Search.route,
            onClick = { navController.navigate(Screen.Search.route) }
        )
    }
}

@Composable
fun HomeScreen(){
    Text(text = "Home")
}

@Composable
fun FavoritesScreen(){
    Text(text = "Favorites")
}

@Composable
fun SearchScreen(){
    Text(text = "Search")
}



