package com.fashionPeople.fashionGuide.data

import android.content.Context
import android.content.SharedPreferences

object AccountAssistant {
    const val PREFS_NAME = "auth"
    const val KEY_TOKEN = "google_token"
    const val KEY_IS_LOGIN = "isLoggedIn"

    fun getPreferences(mContext: Context): SharedPreferences {
        return mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun clearPreferences(context: Context) {
        val prefs = getPreferences(context)
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

    fun setUID(context: Context, uid: String) {
        val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        preferences.edit().apply {
            putString(KEY_TOKEN, uid)
            apply()
        }
    }

    fun getUID(context: Context): String {
        val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val token = preferences.getString(KEY_TOKEN, "") ?: ""
        return token
    }
}