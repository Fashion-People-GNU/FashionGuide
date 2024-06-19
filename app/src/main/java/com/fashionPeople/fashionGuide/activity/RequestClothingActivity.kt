package com.fashionPeople.fashionGuide.activity

import android.content.Intent
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
import com.fashionPeople.fashionGuide.compose.RequestClothingScreen
import com.fashionPeople.fashionGuide.ui.theme.FashionGuideTheme
import com.fashionPeople.fashionGuide.viewmodel.RequestClothingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RequestClothingActivity : ComponentActivity() {
    private val viewModel: RequestClothingViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FashionGuideTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RequestClothingScreen(viewModel)

                }
            }
        }
    }

    private fun observeViewModel(){
        viewModel.closeActivityEvent.observe(this) { event ->
            event.getContentIfNotHandled()?.let {
                finish() // 액티비티 종료
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("clothing", viewModel.currentClothing.value)
                startActivity(intent)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        observeViewModel()
    }



}



