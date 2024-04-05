package com.fashionPeople.fashionGuide.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fashionPeople.fashionGuide.data.Screen

@Composable
fun MainScreen() {
    val screenList = listOf(Screen.Home,Screen.Closet,Screen.Settings)
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController,screenList) },
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.Search, contentDescription = "Search")
            }
        },
        floatingActionButtonPosition = FabPosition.Center

    ) { innerPaddingModifier ->

        NavHost(
            navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPaddingModifier)
        ) {
            composable(Screen.Home.route) {

                HomeScreen()

            }
            composable(Screen.Closet.route) {

                FavoritesScreen()
            }
            composable(Screen.Settings.route) {

                SettingsScreen()

            }
        }

    }
}

@Composable
fun BottomNavigationBar(navController: NavController, items: List<Screen>) {
    NavigationBar{

        val currentBackStackEntry = navController.currentBackStackEntryAsState()

        val currentDestination = currentBackStackEntry.value?.destination

        items.forEach { item ->

            val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
            NavigationBarItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.route) },
                label = { Text(item.route) },
                selected = selected,
                onClick = {

                    if (!selected) {
                        navController.navigate(item.route) {

                            launchSingleTop = true

                            restoreState = true

                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                        }
                    }
                },

                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
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
fun SettingsScreen(){
    Text(text = "Settings")
}



