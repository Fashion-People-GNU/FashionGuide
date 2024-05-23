package com.fashionPeople.fashionGuide.compose

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.fashionPeople.fashionGuide.activity.AddClothingActivity
import com.fashionPeople.fashionGuide.activity.DetailedClothingActivity
import com.fashionPeople.fashionGuide.data.Clothing
import com.fashionPeople.fashionGuide.data.Screen
import com.fashionPeople.fashionGuide.ui.theme.Typography
import com.fashionPeople.fashionGuide.ui.theme.WhiteGray
import com.fashionPeople.fashionGuide.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel) {
    val screenList = listOf(Screen.Home,Screen.Closet,Screen.Settings)
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val closetBottomSheet = rememberModalBottomSheetState()
    val isSheetOpen = viewModel.bottomSheetOpen

    Scaffold(
        bottomBar = { BottomNavigationBar(navController,screenList) },
        floatingActionButton = {
            if (currentRoute == Screen.Closet.route) {
                FloatingActionButton(
                    onClick = {
                        isSheetOpen.value = true
                    }
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
                ClosetScreen(viewModel.clothingLiveData.value!!)
            }
            composable(Screen.Settings.route) {

                SettingsScreen()

            }
        }
        if (isSheetOpen.value) {
            ImagePickerBottomSheet(closetBottomSheet,isSheetOpen)
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagePickerBottomSheet(state: SheetState, isSheetOpen: MutableState<Boolean>) {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            Log.d("test","성공")
            imageUri?.let {
                context.startActivity(AddClothingActivity.newIntent(context, it))
            }
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            context.startActivity(AddClothingActivity.newIntent(context, it))
        }
    }
    ModalBottomSheet(
        sheetState = state,
        onDismissRequest = {
            isSheetOpen.value = false}
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                onClick = {
                    // 카메라 앱 실행
                    val photoUri = createImageUri(context)
                    imageUri = photoUri
                    if(photoUri!=null){
                        cameraLauncher.launch(photoUri)
                    }else{
                        Toast.makeText(context, "Failed to create a URI for the image.", Toast.LENGTH_LONG).show()
                    }


                },
                modifier = Modifier.weight(1f)
            ) {
                Column {
                    Icon(
                        Icons.Filled.CameraAlt,
                        modifier = Modifier.width(50.dp).height(50.dp),
                        contentDescription = "Camera"
                    )
                    Text("카메라", style = Typography.bodyLarge)
                }
            }

            TextButton(
                onClick = {
                    // 갤러리 앱 실행
                    galleryLauncher.launch("image/*")
                },
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Filled.DateRange,
                        modifier = Modifier.width(50.dp).height(50.dp),
                        contentDescription = "Gallery"
                    )
                    Text("갤러리", style = Typography.bodyLarge)
                }
            }
        }
    }
}
private fun createImageUri(context: Context): Uri? {
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.TITLE, "New Photo")
        put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
    }
    return context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
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
fun ClosetScreen(clothes: List<Clothing>){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        WeatherBox()
        GridLayout(clothes)
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
fun GridLayout(clothingList: List<Clothing>) {
    //옷장페이지에서 옷을 불러오는 코드
    val context = LocalContext.current
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        content = {
            items(clothingList.size) { index ->
                val clothing = clothingList[index]
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(WhiteGray)
                        .height(100.dp)
                        .width(100.dp)
                        .clickable {
                            val intent = Intent(context, DetailedClothingActivity::class.java).apply {
                                putExtra("clothing", clothingList[index])
                            }
                            context.startActivity(intent)
                        }
                ) {
                    Image(
                        painter = painterResource(id = clothing.image),
                        contentDescription = "image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(4.dp))
                    )
                }
            }
        }
    )
}

@Composable
fun TestGridItem(clothing: Clothing) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val intent = Intent(context, DetailedClothingActivity::class.java).apply {
                    putExtra("clothing", clothing)
                }
                context.startActivity(intent)
            }
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            painter = painterResource(id = R.drawable.test_item),
            contentDescription = "clothing_image"
        )
        Text(
            text = clothing.name,
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



