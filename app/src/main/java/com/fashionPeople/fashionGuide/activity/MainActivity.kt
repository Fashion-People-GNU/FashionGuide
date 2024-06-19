package com.fashionPeople.fashionGuide.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.core.app.ActivityCompat
import com.fashionPeople.fashionGuide.compose.MainScreen
import com.fashionPeople.fashionGuide.data.Event
import com.fashionPeople.fashionGuide.data.EventList
import com.fashionPeople.fashionGuide.ui.theme.FashionGuideTheme
import com.fashionPeople.fashionGuide.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()



    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.getStringExtra("clothingId") != null){
            viewModel.setClothingId(intent.getStringExtra("clothingId")!!)
            viewModel.setInputStyleDialogScreen(1)
        }
        setContent {
            FashionGuideTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    getPermissions()
                    viewModel.getRegion(this)
                    viewModel.init()
                    MainScreen(viewModel,this)

                }
            }
        }
    }

    private fun getPermissions(){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
            return
        }

    }

    private fun observeViewModel(){
        viewModel.event.observe(this) {
            viewModel.event.value?.getContentIfNotHandled()?.let {
                when(viewModel.event.value?.peekContent() as EventList){
                    EventList.ADD -> {
                        Log.d("test", "observeViewModel")
                        viewModel.getClothingList()
                        Toast.makeText(this,"추가 성공", Toast.LENGTH_SHORT).show()
                    }
                    EventList.DELETE -> {
                        Log.d("test", "observeViewModel")
                        viewModel.getClothingList()
                        Toast.makeText(this,"삭제 성공", Toast.LENGTH_SHORT).show()
                    }
                    EventList.CLOTHING_SELECT -> {
                        Log.d("test", "observeViewModel")
                        finish()
                        val intent = Intent(this, RequestClothingActivity::class.java)
                        startActivity(intent)
                    }
                    EventList.USER_LOAD -> {
                        checkUserInfo()
                    }
                }
            }
        }
    }
    private fun checkUserInfo(){
        val userInfo = viewModel.userInfo.value
        if (userInfo?.age == "" || userInfo?.sex == ""){
            startActivity(Intent(this, UserRegistrationActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        Log.d("test", "onResume")
    }

    override fun onStop() {
        super.onStop()
        Log.d("test", "onStop")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d("test", "onDestroy")
    }

}



