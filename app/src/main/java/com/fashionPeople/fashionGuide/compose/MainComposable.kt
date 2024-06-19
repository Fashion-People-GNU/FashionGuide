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
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.fashionPeople.fashionGuide.R
import com.fashionPeople.fashionGuide.activity.AddClothingActivity
import com.fashionPeople.fashionGuide.activity.DetailedClothingActivity
import com.fashionPeople.fashionGuide.activity.LoginActivity
import com.fashionPeople.fashionGuide.data.Clothing
import com.fashionPeople.fashionGuide.data.RecommendOption
import com.fashionPeople.fashionGuide.data.Screen
import com.fashionPeople.fashionGuide.data.TodayDate
import com.fashionPeople.fashionGuide.data.Weather
import com.fashionPeople.fashionGuide.ui.theme.Typography
import com.fashionPeople.fashionGuide.ui.theme.WhiteGray
import com.fashionPeople.fashionGuide.utils.LoginUtils
import com.fashionPeople.fashionGuide.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel,activity: Activity) {
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
                    onClick = {
                        if(viewModel.isTabScreen.value == 0){
                            viewModel.setInputStyleDialogScreen(1)
                        }else{
                            viewModel.selectClothingActivity()
                        }

                    }
                ) {
                    Icon(Icons.Filled.Search, contentDescription = "Search")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center

    ) { innerPaddingModifier ->
        Box{
            NavHost(
                navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.padding(innerPaddingModifier)
            ) {
                composable(Screen.Home.route) {

                    HomeScreen(viewModel)

                }
                composable(Screen.Closet.route) {
                    ClosetScreen(viewModel)
                }
                composable(Screen.Settings.route) {

                    SettingsScreen(viewModel,activity)

                }
            }
            if (isSheetOpen.value) {
                ImagePickerBottomSheet(closetBottomSheet,isSheetOpen)
            }
            if(viewModel.isLoading.value){
                Log.d("test", "loading")
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
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
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Filled.CameraAlt,
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp),
                        contentDescription = "Camera",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text="카메라",
                        style = Typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground)
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
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp),
                        tint = MaterialTheme.colorScheme.onBackground,
                        contentDescription = "Gallery"
                    )
                    Text(
                        text="갤러리",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = Typography.bodyLarge,
  )
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
    Box(
        modifier = Modifier
            .graphicsLayer { }
            .shadow(
                elevation = 16.dp,
                shape = MaterialTheme.shapes.medium,
                clip = false,
                ambientColor = if (isSystemInDarkTheme()) Color.White else Color.Gray,
                spotColor = if (isSystemInDarkTheme()) Color.White else Color.Gray
            )
    ){
        NavigationBar(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
            tonalElevation = 0.dp,
            containerColor = MaterialTheme.colorScheme.background
        ){

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
                        selectedIconColor = MaterialTheme.colorScheme.onBackground,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                        selectedTextColor = MaterialTheme.colorScheme.onBackground,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                        indicatorColor = MaterialTheme.colorScheme.background,
                    )
                )
            }
        }
    }

}

@Composable
fun CustomTabs(viewModel: MainViewModel) {
    var selectedIndex = viewModel.isTabScreen.value

    val list = listOf("전체 추천", "부분 추천")

    TabRow(
        selectedTabIndex = selectedIndex,
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.background)
            .padding(1.dp),
        indicator = { Box {} },
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        list.forEachIndexed { index, text ->
            val selected = selectedIndex == index
            Tab(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(
                        if (selected) MaterialTheme.colorScheme.onBackground
                        else MaterialTheme.colorScheme.background
                    ),
                selected = selected,
                onClick = {
                    selectedIndex = index
                    viewModel.setTabScreen(index) },
                text = {
                    Text(
                        text = text,
                        color = if (selected) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onBackground
                    )
                }
            )
        }
    }
}

@Composable
fun HomeScreen(viewModel: MainViewModel){
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

        CustomTabs(viewModel)
        WeatherBox(viewModel.weather.value,viewModel.region.value,viewModel.todayDate.value)

        if (viewModel.isTabScreen.value == 0) {
            EntireRecommendation()
        } else {
            PartialRecommendation(viewModel.currentClothing.value)
        }
        if (viewModel.isResultDialogScreen.value == 1) {
            ResultDialogScreen(viewModel)
        }
        if (viewModel.isInputStyleDialogScreen.value == 1) {
            InputStyleDialogScreen(viewModel)
        }


    }
}

@Composable
fun EntireRecommendation(){
    Text(
        modifier = Modifier.fillMaxWidth(),
        fontSize = 16.sp,
        textAlign = TextAlign.Center,
        text = "상 하의를 동시에 추천하는 방식")
    //동그란 이미지로 나오게 하기

    Image(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .shadow(
                elevation = 4.dp, // 그림자의 높이를 조정할 수 있습니다.
                clip = true
            ),
        alignment = Alignment.Center,
        painter = painterResource(id = R.drawable.content_image),
        contentDescription = "content")
}

@Composable
fun PartialRecommendation(currentClothing: Clothing?){
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            text = "상 하의중 하나를 추천하는 방식")
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .shadow(
                    elevation = 4.dp, // 그림자의 높이를 조정할 수 있습니다.
                    clip = true
                ),
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
            painter = if (currentClothing!=null) rememberAsyncImagePainter(model = currentClothing.imageUrl)
            else painterResource(id = R.drawable.test_item),
            contentDescription = "content")
    }

}

@Composable
fun ClosetScreen(viewModel: MainViewModel){
    val context = LocalContext.current
    val clothingList by viewModel.clothingLiveData.observeAsState()
    val weather by viewModel.weather
    val todayDate by viewModel.todayDate
    val region by viewModel.region

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
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
                    text = "옷장")
            }

            WeatherBox(weather,region,todayDate)
            clothingList?.let { GridLayout(it){
                clothing ->
                val intent =
                    Intent(context, DetailedClothingActivity::class.java).apply {
                        putExtra("clothing", clothing)
                    }
                context.startActivity(intent)
            } }

        }
        if (viewModel.isLoading.value) {
            Log.d("test", "loading")
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

    }

}

@Composable
private fun WeatherBox(weather: Weather,region: String,todayDate: TodayDate) {
    var weatherId = R.drawable.sun
    when(weather.weather){
        "맑음" -> weatherId = R.drawable.sun
        "구름 많음" -> weatherId = R.drawable.cloud
        "비" -> weatherId = R.drawable.rain
        "눈" -> weatherId = R.drawable.snow
        "안개" -> weatherId = R.drawable.fog
    }
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
            text = region
        )
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.surface)

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Text(
                text = "${todayDate.month}월 ${todayDate.day}일 ",
                fontSize = 20.sp,
            )
            Text(
                text = todayDate.dayOfWeek,
                fontSize = 20.sp,
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Image(
                modifier = Modifier
                    .size(35.dp)
                    .padding(4.dp),
                painter = painterResource(id = R.drawable.high_temp),
                contentDescription = "high_temp"
            )
            Text(
                text = weather.maxTemp.toString(),
            )
            Text(
                text = "/",
            )
            Image(
                modifier = Modifier
                    .size(35.dp)
                    .padding(4.dp),
                painter = painterResource(id = R.drawable.low_temp),
                contentDescription = "low_temp"
            )
            Text(
                text = weather.minTemp.toString(),
            )
            Text(
                text = "°C",
            )
            Image(
                modifier = Modifier
                    .size(35.dp)
                    .padding(4.dp),
                painter = painterResource(id = weatherId),
                contentDescription = "weather"
            )
        }
    }
}

@Composable
fun GridLayout(clothingList: List<Clothing>,onClothingClick: (Clothing) -> Unit) {
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
                            onClothingClick(clothing)
                        }
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = clothing.imageUrl),
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
fun SettingsScreen(viewModel: MainViewModel, activity: Activity){
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
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
                text = "설정")
        }
        if (viewModel.isRegionDialogScreen.value == 1) {
            RegionDialogScreen(viewModel)
        }
        if (viewModel.isSourcesDialogScreen.value == 1) {
            SourcesDialogScreen(viewModel)
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(WhiteGray)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    viewModel.getRegion(context as Activity)
                    viewModel.setRegionDialogScreen(1)
                },
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                style = Typography.bodyMedium,
                text = "지역 설정")
        }

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
                .padding(16.dp)
                .clickable {
                    viewModel.setSourcesDialogScreen(1)
                },
            style = Typography.bodyMedium,
            text = "데이터 출처")
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
                .padding(16.dp)
                .clickable {
                    LoginUtils(coroutineScope, activity, context).logout()
                    activity.finish()
                    activity.startActivity(Intent(context, LoginActivity::class.java))
                },
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

@Composable
fun RegionDialogScreen(viewModel: MainViewModel){
    val region by viewModel.region
    Dialog(onDismissRequest = { viewModel.setRegionDialogScreen(0) }) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text("지역 설정")
            Spacer(modifier = Modifier.height(8.dp))
            Text("현재 사는 지역: $region", style = Typography.bodyMedium)
            Text("설정 완료", style = Typography.bodyMedium)
        }
    }
}

@Composable
fun SourcesDialogScreen(viewModel: MainViewModel){
    Dialog(onDismissRequest = { viewModel.setSourcesDialogScreen(0) }) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text("데이터 출처")
            Spacer(modifier = Modifier.height(8.dp))
            Text("날씨 아이콘 - 기상청 공공누리", style = Typography.bodyMedium)
        }
    }
}

@Composable
fun ResultDialogScreen(viewModel: MainViewModel){
    val recommendedClothingList = viewModel.recommendedClothingLiveData.value
    var currentIndex by remember { mutableStateOf(0) }
    Dialog(onDismissRequest = { viewModel.setResultDialogScreen(0) }) {
        Surface(
            modifier = Modifier
                .border(
                    BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(16.dp)
                ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .background(MaterialTheme.colorScheme.surface),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("모델 결과",color=MaterialTheme.colorScheme.onBackground)
                Spacer(modifier = Modifier.height(8.dp))
                if(recommendedClothingList?.size == 1 && !recommendedClothingList[0].id.isNullOrEmpty()){
                    Text("* 결과가 하나만 존재합니다",color= Color.Red, style = Typography.bodyMedium)
                }
                Spacer(modifier = Modifier.height(8.dp))
                if (!recommendedClothingList.isNullOrEmpty() && !recommendedClothingList[0].id.isNullOrEmpty()){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            modifier = Modifier.size(48.dp),
                            onClick = {
                                if (currentIndex > 0) {
                                    currentIndex-=1
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Previous",
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .size(150.dp)
                                .background(Color.Gray, shape = RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ){
                            Image(
                                modifier = Modifier
                                    .size(150.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop,
                                painter = rememberAsyncImagePainter(model = recommendedClothingList[currentIndex].imageUrl),
                                contentDescription = "Centered Image"
                            )
                        }

                        IconButton(
                            modifier = Modifier.size(48.dp),
                            onClick = {
                                if (currentIndex < recommendedClothingList.size - 1) {
                                    currentIndex+=1
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Next",
                                modifier = Modifier.size(32.dp)
                            )
                        }

                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(recommendedClothingList[currentIndex].type,color=MaterialTheme.colorScheme.onBackground)
                    Button(
                        onClick = { viewModel.setResultDialogScreen(0) },
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {
                        Text("확인")
                    }
                }else{
                    Text("옷 추천 결과가 없습니다.", style = Typography.bodyMedium)
                }


            }
        }
    }

}

@Composable
fun InputStyleDialogScreen(viewModel: MainViewModel){
    val option = if(viewModel.isTabScreen.value == 0) RecommendOption.Entire else RecommendOption.Partial
    Dialog(onDismissRequest = { viewModel.setInputStyleDialogScreen(0) }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text ="스타일을 선택하세요"
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                if (option == RecommendOption.Entire) {
                    viewModel.entireClothingList("클래식")
                }else{
                    viewModel.partialClothingList("클래식",viewModel.currentClothing.value!!.id)
                }
                viewModel.setInputStyleDialogScreen(0)
            }) {
                Text("클래식")
            }
            Button(onClick = {
                if (option == RecommendOption.Entire) {
                    viewModel.entireClothingList("스트리트")
                }else{
                    viewModel.partialClothingList("스트리트",viewModel.currentClothing.value!!.id)
                }
                viewModel.setInputStyleDialogScreen(0)
            }) {
                Text("스트리트")
            }
            Button(onClick = {
                if (option == RecommendOption.Entire) {
                    viewModel.entireClothingList("러블리")
                }else{
                    viewModel.partialClothingList("러블리",viewModel.currentClothing.value!!.id)
                }
                viewModel.setInputStyleDialogScreen(0)
            }) {
                Text("러블리")

            }
        }
    }
}




