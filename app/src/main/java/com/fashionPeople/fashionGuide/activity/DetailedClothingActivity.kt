package com.fashionPeople.fashionGuide.activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.fashionPeople.fashionGuide.ClothingRepository
import com.fashionPeople.fashionGuide.compose.DetailedClothingScreen
import com.fashionPeople.fashionGuide.data.Clothing
import com.fashionPeople.fashionGuide.ui.theme.FashionGuideTheme
import com.fashionPeople.fashionGuide.viewmodel.DetailedClothingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailedClothingActivity : ComponentActivity() {
    private val viewModel: DetailedClothingViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val clothing = intent.getParcelableExtra("clothing",Clothing::class.java)

            // clothing이 null인 경우를 고려하여 안전하게 ViewModel에 설정

            if (clothing != null) {
                viewModel.setCloth(clothing)
            } else {
                // clothing이 null인 경우의 처리 로직
                Log.d("test","옷 못 받음")
            }
            FashionGuideTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DetailedClothingScreen(this,viewModel)
                }
            }
        }
    }
}
