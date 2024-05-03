package com.fashionPeople.fashionGuide.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.fashionPeople.fashionGuide.ClothingRepository
import com.fashionPeople.fashionGuide.compose.DetailedClothingScreen
import com.fashionPeople.fashionGuide.ui.theme.FashionGuideTheme
import com.fashionPeople.fashionGuide.viewmodel.DetailedClothingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailedClothingActivity : ComponentActivity() {
    private val viewModel: DetailedClothingViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            viewModel.setCloth()
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
