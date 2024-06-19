package com.fashionPeople.fashionGuide.activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.fashionPeople.fashionGuide.compose.RequestClothingScreen
import com.fashionPeople.fashionGuide.compose.UserRegistrationScreen
import com.fashionPeople.fashionGuide.data.EventList
import com.fashionPeople.fashionGuide.ui.theme.FashionGuideTheme
import com.fashionPeople.fashionGuide.viewmodel.RequestClothingViewModel
import com.fashionPeople.fashionGuide.viewmodel.UserRegistrationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserRegistrationActivity : ComponentActivity() {
    private val viewModel: UserRegistrationViewModel by viewModels()

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
                    UserRegistrationScreen(viewModel)

                }
            }
        }
    }

    private fun observeViewModel(){
        viewModel.closeActivityEvent.observe(this) { event ->
            event.getContentIfNotHandled()?.let {
                finish() // 액티비티 종료
            }
        }
    }

    override fun onStart() {
        super.onStart()
        observeViewModel()
    }



}



