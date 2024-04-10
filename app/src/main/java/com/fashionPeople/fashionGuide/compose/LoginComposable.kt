package com.fashionPeople.fashionGuide.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.fashionPeople.fashionGuide.R
import com.fashionPeople.fashionGuide.ui.theme.Typography
import com.fashionPeople.fashionGuide.ui.theme.WhiteGray


@Composable
fun LoginScreen() {

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) { innerPaddingModifier ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPaddingModifier),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text="Welcome")
            Text("패션가이드")
            Spacer(modifier = Modifier.size(10.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {

                          //구글 로그인 함수 넣으면 돼

                },
                colors= ButtonDefaults.buttonColors(
                    containerColor = WhiteGray
                ),
            ){
                Image(
                    modifier = Modifier.size(40.dp),
                    painter = painterResource(id = R.drawable.ic_logo_google),
                    contentDescription = "google",
                )
                Spacer(modifier = Modifier.size(10.dp))
                Text(
                    style = Typography.bodyMedium,
                    color = Color.Black,
                    text="Sign in with Google")
            }

        }

    }
}




