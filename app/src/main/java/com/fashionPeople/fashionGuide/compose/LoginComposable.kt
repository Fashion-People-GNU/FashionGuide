package com.fashionPeople.fashionGuide.compose

import android.content.Context
import android.util.Log
import android.widget.Toast
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.fashionPeople.fashionGuide.R
import com.fashionPeople.fashionGuide.ui.theme.Typography
import com.fashionPeople.fashionGuide.ui.theme.WhiteGray
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID


@Composable
fun LoginScreen(context: Context) {

    val coroutineScope = rememberCoroutineScope()

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
                    try {
                        val credentialManager = CredentialManager.create(context)

                        val rawNonce = UUID.randomUUID().toString()
                        val bytes = rawNonce.toByteArray()
                        val md = MessageDigest.getInstance("SHA-256")
                        val digest = md.digest(bytes)
                        val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }

                        Log.d("test",R.string.server_client_id.toString())
                        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
                            .setFilterByAuthorizedAccounts(false)
                            .setServerClientId("46367136175-faq39nrqar4b8m07cvij010v79i0u77p.apps.googleusercontent.com")
                            .setNonce(hashedNonce)
                            .build()

                        val request: GetCredentialRequest = GetCredentialRequest.Builder()
                            .addCredentialOption(googleIdOption)
                            .build()

                        coroutineScope.launch {
                            val result = credentialManager.getCredential(
                                request = request,
                                context = context,
                            )

                            val credential = result.credential

                            val googleIdTokenCredential = GoogleIdTokenCredential
                                .createFrom(credential.data)

                            val googleIdToken = googleIdTokenCredential.idToken

                            Toast.makeText(context, "Sign In!", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: GetCredentialException) {
                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    } catch (e: GoogleIdTokenParsingException) {
                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    }
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




