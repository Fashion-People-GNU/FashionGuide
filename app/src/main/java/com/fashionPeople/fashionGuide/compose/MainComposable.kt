package com.fashionPeople.fashionGuide.compose

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fashionPeople.fashionGuide.R
import com.fashionPeople.fashionGuide.data.Screen
import com.fashionPeople.fashionGuide.ui.theme.Typography
import com.fashionPeople.fashionGuide.ui.theme.WhiteGray

@Composable
fun MainScreen() {
    val screenList = listOf(Screen.Home,Screen.Closet,Screen.Settings)
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    Scaffold(
        bottomBar = { BottomNavigationBar(navController,screenList) },
        floatingActionButton = {
            if (currentRoute == Screen.Closet.route) {
                FloatingActionButton(
                    onClick = { /* TODO */ }
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add")
                }
            }
            if (currentRoute == Screen.Home.route) {
                FloatingActionButton(
                    onClick = { /* TODO */ }
                ) {
                    Icon(Icons.Filled.Search, contentDescription = "Search")
                }
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

                ClosetScreen()
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
fun CustomTabs() {
    var selectedIndex by remember { mutableIntStateOf(0) }

    val list = listOf("전체 추천", "부분 추천")

    TabRow(selectedTabIndex = selectedIndex,
        containerColor = Color.DarkGray,
        contentColor = Color.DarkGray,
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clip(RoundedCornerShape(50))
            .padding(1.dp),
        indicator = {
            Box {}
        }
    ) {
        list.forEachIndexed { index, text ->
            val selected = selectedIndex == index
            Tab(
                modifier = if (selected) Modifier
                    .clip(RoundedCornerShape(50))
                    .background(
                        Color.White
                    )
                else Modifier
                    .clip(RoundedCornerShape(50))
                    .background(
                        Color.DarkGray
                    ),
                selected = selected,
                onClick = { selectedIndex = index },
                text = { Text(text = text, color = Color.Black) }
            )
        }
    }
}

@Composable
fun HomeScreen(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),

        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = "오늘의 옷 추천")
        }

        CustomTabs()
        WeatherBox()
        Text(
            modifier = Modifier.fillMaxWidth(),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            text =
            "여러 요소를 고려하여 상하의를 동시에 추천하는 방식")
        Image(
            modifier = Modifier
                .padding(16.dp),
            alignment = Alignment.Center,
            painter = painterResource(id = R.drawable.content_image),
            contentDescription = "content")

    }
}

@Composable
fun ClosetScreen(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        WeatherBox()
        GridLayout()
    }
}

@Composable
private fun WeatherBox() {
    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Left,
        fontSize = 24.sp,
        text = "오늘의 날씨 정보")
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            modifier = Modifier.padding(8.dp)
            ,
            imageVector = Icons.Filled.Place,
            contentDescription = "place"
        )
        Text(
            text = "서울시"
        )
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(WhiteGray)

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Text(
                text = "금 ",
                fontSize = 20.sp,
                color = Color.Black
            )
            Text(
                text = "4월 19일",
                fontSize = 20.sp,
                color = Color.LightGray
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Text(
                text = "24",
                color = Color.Black
            )
            Text(
                text = "/",
                color = Color.Black
            )
            Text(
                text = "13",
                color = Color.Black
            )
            Text(
                text = "°C",
                color = Color.Black
            )
            Image(
                modifier = Modifier.padding(4.dp),
                painter = painterResource(id = R.drawable.sun),
                contentDescription = "weather"
            )
        }
    }
}

@Composable
fun GridLayout() {
    val items = (1..20).toList()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.Center,
        verticalArrangement = Arrangement.Center
    ) {
        items(items.size) { index ->
            GridItem(index = index)
        }
    }
}

@Composable
fun GridItem(index: Int) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            painter = painterResource(id = R.drawable.test_item),
            contentDescription = "test_image"
        )
        Text(
            text = "옷 $index",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
    }

}

@Composable
fun SettingsScreen(){
    Column {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            text = "앱 설정")
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(WhiteGray)
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            style = Typography.bodyMedium,
            text = "지역 설정")
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(1.dp)
                .background(WhiteGray)
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            style = Typography.bodyMedium,
            text = "사용자 스타일 설정")
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(1.dp)
                .background(WhiteGray)
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            style = Typography.bodyMedium,
            text = "로그아웃")
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(1.dp)
                .background(WhiteGray)
        )
    }
}



