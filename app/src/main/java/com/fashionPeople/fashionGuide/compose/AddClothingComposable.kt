package com.fashionPeople.fashionGuide.compose

import android.app.Activity
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.fashionPeople.fashionGuide.ClothingApi
import com.fashionPeople.fashionGuide.NetworkModule
import com.fashionPeople.fashionGuide.activity.AddClothingActivity
import com.fashionPeople.fashionGuide.viewmodel.AddClothingViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddClothingScreen(viewModel: AddClothingViewModel) {
    val context = LocalContext.current
    val imagePart = remember { viewModel.createImagePartFromUri(context, viewModel.clothingUri.value!!)}
    Scaffold(
        topBar = { TopAppBar(
            title = { Text("내 옷 추가") },
            navigationIcon = {
                IconButton(onClick = { (context as? AddClothingActivity)?.finish() }) {
                    Icon(imageVector = Icons.Filled.ArrowBackIosNew, contentDescription = "back")
                }
            }
        ) },
    ) { innerPaddingModifier ->
        Column(modifier = Modifier.padding(innerPaddingModifier)){
            val painter = rememberAsyncImagePainter(
                model = viewModel.clothingUri.value
            )
            var text by remember { mutableStateOf(TextFieldValue()) } // 에딧 텍스트 값을 상태로 관리
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop,
                painter = painter, contentDescription = "image"
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color(0xFFF6F6F6)),
                value = text,
                onValueChange = { text = it },
                label = {
                    Text("옷 이름")
                }
            )
            Spacer(modifier = Modifier.weight(1f)) // 나머지 공간을 채우는 Spacer
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(0.dp),
                onClick = { viewModel.addClothing(imagePart) }
            ) {
                Text(text = "저장")
            }
        }
    }
}









