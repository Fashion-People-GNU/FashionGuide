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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import coil.compose.rememberAsyncImagePainter
import com.fashionPeople.fashionGuide.AccountAssistant
import com.fashionPeople.fashionGuide.R
import com.fashionPeople.fashionGuide.activity.AddClothingActivity
import com.fashionPeople.fashionGuide.activity.DetailedClothingActivity
import com.fashionPeople.fashionGuide.activity.RequestClothingActivity
import com.fashionPeople.fashionGuide.activity.UserRegistrationActivity
import com.fashionPeople.fashionGuide.data.Clothing
import com.fashionPeople.fashionGuide.data.Screen
import com.fashionPeople.fashionGuide.ui.theme.Cold100
import com.fashionPeople.fashionGuide.ui.theme.Typography
import com.fashionPeople.fashionGuide.ui.theme.WhiteGray
import com.fashionPeople.fashionGuide.viewmodel.MainViewModel
import com.fashionPeople.fashionGuide.viewmodel.RequestClothingViewModel
import com.fashionPeople.fashionGuide.viewmodel.UserRegistrationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRegistrationScreen(viewModel: UserRegistrationViewModel) {
    val ageGroups = listOf("10대", "20대", "30대", "40대")
    val genders = listOf("남성", "여성")
    var selectedAgeGroup by remember { mutableStateOf<String?>(ageGroups[0]) }
    var selectedGender by remember { mutableStateOf<String?>(genders[0]) }
    val uid = AccountAssistant.getUID()

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

            title = { Text("유저 정보 입력") }
        ) },
    ) { innerPaddingModifier ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPaddingModifier),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("연령대를 선택하세요",style=Typography.bodyLarge)
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ageGroups.forEach { ageGroup ->
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (selectedAgeGroup == ageGroup)
                                        Cold100
                                    else MaterialTheme.colorScheme.surface
                                )
                                .clickable { selectedAgeGroup = ageGroup }
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = ageGroup, style = Typography.bodyMedium)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text("성별을 선택하세요", style = Typography.bodyLarge)
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    genders.forEach { gender ->
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (selectedGender == gender)
                                        Cold100
                                    else MaterialTheme.colorScheme.surface
                                )
                                .clickable { selectedGender = gender }
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = gender, style = Typography.bodyMedium)
                        }
                    }
                }
            }
            Button(
                onClick = {
                    viewModel.updateUserInfo(uid!!, selectedAgeGroup!!, selectedGender!!)
                    viewModel.closeActivity()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(4.dp),
            ) {
                Text("제출", style = Typography.bodyLarge)
            }
        }

    }
}





