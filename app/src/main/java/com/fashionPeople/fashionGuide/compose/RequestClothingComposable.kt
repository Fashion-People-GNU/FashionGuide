package com.fashionPeople.fashionGuide.compose

import android.content.Intent
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import coil.compose.rememberAsyncImagePainter
import com.fashionPeople.fashionGuide.R
import com.fashionPeople.fashionGuide.activity.AddClothingActivity
import com.fashionPeople.fashionGuide.activity.DetailedClothingActivity
import com.fashionPeople.fashionGuide.activity.RequestClothingActivity
import com.fashionPeople.fashionGuide.data.Clothing
import com.fashionPeople.fashionGuide.data.Screen
import com.fashionPeople.fashionGuide.ui.theme.Typography
import com.fashionPeople.fashionGuide.ui.theme.WhiteGray
import com.fashionPeople.fashionGuide.viewmodel.MainViewModel
import com.fashionPeople.fashionGuide.viewmodel.RequestClothingViewModel

//옷장 화면 액티비티
//옷장에 옷 하나를 선택해 전송하는 화면
//옷을 선택하면 옷의 정보를 서버로 전송
//옷의 정보는 옷의 이름, 옷의 종류, 옷의 색상, 옷의 이미지
//옷의 이름은 텍스트로 입력받음

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestClothingScreen(viewModel: RequestClothingViewModel) {
    val clothingList by viewModel.clothingLiveData.observeAsState()
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
            topBar = { TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                titleContentColor = MaterialTheme.colorScheme.onBackground,
                navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
            ),

            title = { Text("전송 할 옷 선택") },
            navigationIcon = {
                IconButton(onClick = { (context as? RequestClothingActivity)?.finish() }) {
                    Icon(imageVector = Icons.Filled.ArrowBackIosNew, contentDescription = "back")
                }
            }
        ) },
    ) { innerPaddingModifier ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPaddingModifier),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            clothingList?.let { GridLayout(it){clothing ->
                viewModel.setSubmitDialogScreen(1)
            } }

        }
        if (viewModel.isSubmitDialogScreen.value == 1) {
            SubmitClothingDialog(viewModel)
        }

    }
}
@Composable
fun SubmitClothingDialog(viewModel: RequestClothingViewModel){
    Dialog(onDismissRequest = { viewModel.setSubmitDialogScreen(0) }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text ="해당 옷을 전송하시겠습니까?",
                style = Typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f).padding(8.dp),
                    onClick = { /*TODO*/ },
                ) {
                    Text("예")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f).padding(8.dp),
                    onClick = { /*TODO*/ },
                ) {
                    Text("아니요")
                }
            }
        }
    }

}

