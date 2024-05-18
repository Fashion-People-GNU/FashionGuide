package com.fashionPeople.fashionGuide

import android.content.Context
import android.content.SharedPreferences

object AccountAssistant {
    const val PREFS_NAME = "auth"
    const val KEY_IS_LOGIN = "isLoggedIn"
    private const val KEY_TOKEN = "google_token"

    private lateinit var sharedPreferences: SharedPreferences

    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getPreferences(mContext: Context): SharedPreferences {
        return mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun clearPreferences() {
        val editor = sharedPreferences.edit()
        editor.remove(KEY_TOKEN)
        editor.apply()
    }

    fun setUID(uid: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_TOKEN, uid)
        editor.apply()
    }

    fun getUID(): String? {
        return sharedPreferences.getString(KEY_TOKEN, null)
    }
}