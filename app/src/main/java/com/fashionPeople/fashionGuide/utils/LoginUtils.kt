package com.fashionPeople.fashionGuide.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.fashionPeople.fashionGuide.activity.MainActivity
import com.fashionPeople.fashionGuide.AccountAssistant
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID

class LoginUtils(coroutineScope: CoroutineScope, activity: Activity, localContext: Context) {

    private var mAuth: FirebaseAuth = Firebase.auth
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var context: Context
    private var coroutineScope: CoroutineScope
    private var activity: Activity
    private var localContext: Context

    init {
        this.activity = activity
        this.context = activity.baseContext
        this.coroutineScope = coroutineScope
        this.localContext = localContext
    }

    fun googleLogin() {
        try {
            val credentialManager = CredentialManager.create(localContext)

            val rawNonce = UUID.randomUUID().toString()
            val bytes = rawNonce.toByteArray()
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest(bytes)
            val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }

            val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId("513763739759-o680otdkt5iro5t7krgnvd149f4btpph.apps.googleusercontent.com")
                .setNonce(hashedNonce)
                .build()

            val request: GetCredentialRequest = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            coroutineScope.launch {
                val result = credentialManager.getCredential(
                    request = request,
                    context = localContext,
                )

                val credential = result.credential

                val googleIdTokenCredential = GoogleIdTokenCredential
                    .createFrom(credential.data)

                val googleIdToken = googleIdTokenCredential.idToken
                Log.d("id", googleIdToken)

                firebaseLogin(googleIdToken)

            }
        } catch (e: GetCredentialException) {
            Log.e("LoginUtils", "GetCredentialException", e)
        } catch (e: GoogleIdTokenParsingException) {
            Log.e("LoginUtils", "GoogleIdTokenParsingException", e)
        }
    }

    private fun firebaseLogin(idToken:String) {
        // Got an ID token from Google. Use it to authenticate
        // with Firebase.
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(firebaseCredential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    Log.d("firebaseLogin", "signInWithCredential:success")
                    val user = mAuth.currentUser
                    saveUserToFirestore(user)
                } else {
                    Log.w("firebaseLogin", "signInWithCredential:failure", task.exception)
                }
            }
    }

    private fun saveUserToFirestore(user: FirebaseUser?) {
        user?.let {
            val userId = it.uid
            val email = it.email
            val displayName = it.displayName

            val userData = hashMapOf(
                "email" to email,
                "displayName" to displayName
            )

            db.collection("users").document(userId)
                .set(userData)
                .addOnSuccessListener {
                    Log.d("LoginUtils", "saveUserToFirestore")
                    AccountAssistant.setUID(userId)
                    val preferences = activity.getSharedPreferences(AccountAssistant.PREFS_NAME, Context.MODE_PRIVATE)
                    preferences.edit().apply {
                        putBoolean(AccountAssistant.KEY_IS_LOGIN, true)
                        apply()
                    }
                    val i = Intent(context, MainActivity::class.java)
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(i)
                }
                .addOnFailureListener { e ->
                    Log.e("LoginUtils", "saveUserToFirestore", e)
                }
        }
    }

    fun autoLogin() {
        val preferences = activity.getSharedPreferences(AccountAssistant.PREFS_NAME, Context.MODE_PRIVATE)
        if (preferences.getBoolean(AccountAssistant.KEY_IS_LOGIN, false)) {
            val i = Intent(context, MainActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.finish()
            activity.startActivity(i)
        }
    }

    fun logout() {
        AccountAssistant.clearPreferences()
        val preferences = activity.getSharedPreferences(AccountAssistant.PREFS_NAME, Context.MODE_PRIVATE)
        preferences.edit().apply {
            putBoolean(AccountAssistant.KEY_IS_LOGIN, false)
            apply()
        }
    }

}
