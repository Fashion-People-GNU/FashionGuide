package com.fashionPeople.fashionGuide.compose

import android.app.Activity
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.Toast
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.fashionPeople.fashionGuide.ui.theme.Typography
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
    viewModel.errorMessage.observe(context as AddClothingActivity) { event ->
        event.getContentIfNotHandled()?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = { TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                titleContentColor = MaterialTheme.colorScheme.onBackground,
                navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
            ),

            title = { Text("내 옷 추가") },
            navigationIcon = {
                IconButton(onClick = { (context as? AddClothingActivity)?.finish() }) {
                    Icon(imageVector = Icons.Filled.ArrowBackIosNew, contentDescription = "back")
                }
            }
        ) },
    ) { innerPaddingModifier ->
        Box {
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                ) {

                    Text("옷 이름")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        maxLines = 1,
                        textStyle = Typography.bodyMedium,
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.onBackground,
                            unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                            focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                        ),
                        value = text,
                        onValueChange = {
                            text = it
                            viewModel.setClothingName(text.text)
                        })

                }
            }
            if(viewModel.isLoading.value){
                Log.d("test", "loading")
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }else{
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    shape = RoundedCornerShape(0.dp),
                    onClick = {
                        viewModel.addClothing(imagePart)
                        Log.d("test", viewModel.isLoading.value.toString())}
                ) {
                    Text(text = "저장", style = Typography.bodyLarge)
                    //로딩화면
                }
            }

        }

    }
    //옷장에서 옷을 추가하는 화면
    //이미지를 선택하고, 옷 이름을 입력하고, 저장 버튼을 누르면 옷장에 옷이 추가됨
    //이미지는 uri로 받아옴
    //이미지를 서버에 업로드하고, 옷 이름과 함께 저장
    //저장 버튼을 누르면 옷장으로 이동
    //뒤로가기 버튼을 누르면 이전 화면으로 이동
    //이미지를 선택하면 이미지가 화면에 나타남
    //옷 이름을 입력하면 옷 이름이 저장됨
    //저장 버튼을 누르면 옷 이름과 이미지가 서버에 저장됨
    //저장 버튼을 누르면 옷장으로 이동
}









