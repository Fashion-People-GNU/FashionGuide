package com.fashionPeople.fashionGuide.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.FixedScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.fashionPeople.fashionGuide.ClothingRepository
import com.fashionPeople.fashionGuide.compose.AddClothingScreen
import com.fashionPeople.fashionGuide.compose.DetailedClothingScreen
import com.fashionPeople.fashionGuide.data.SizeOption
import com.fashionPeople.fashionGuide.ui.theme.FashionGuideTheme
import com.fashionPeople.fashionGuide.viewmodel.AddClothingViewModel
import com.fashionPeople.fashionGuide.viewmodel.DetailedClothingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddClothingActivity : ComponentActivity() {
    private val viewModel: AddClothingViewModel by viewModels()
    companion object {
        fun newIntent(context: Context, imageUri: Uri): Intent {
            return Intent(context, AddClothingActivity::class.java).apply {
                putExtra("image_uri", imageUri.toString())
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val imageUri = intent.getStringExtra("image_uri")?.let { Uri.parse(it) }

        setContent {
            FashionGuideTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    viewModel.init()
                    imageUri?.let {
                        viewModel.setClothingUri(imageUri)
                        Log.d("test",viewModel.clothingUri.value.toString())
                        AddClothingScreen(viewModel = viewModel)
                    }

                }
            }
        }
    }
}
