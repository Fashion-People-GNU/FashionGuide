package com.fashionPeople.fashionGuide.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.fashionPeople.fashionGuide.compose.AddClothingScreen
import com.fashionPeople.fashionGuide.ui.theme.FashionGuideTheme
import com.fashionPeople.fashionGuide.viewmodel.AddClothingViewModel
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
        observeViewModel()
        setContent {
            FashionGuideTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    viewModel.init()
                    imageUri?.let {
                        viewModel.setClothingUri(imageUri)
                        AddClothingScreen(viewModel = viewModel)
                    }

                }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.closeActivityEvent.observe(this) { event ->
            event.getContentIfNotHandled()?.let {
                finish() // 액티비티 종료

            }
        }
    }
}
