package com.fashionPeople.fashionGuide

object AppManager {
    private var idToken: String? = null

    fun saveIdToken(idToken: String) {
        this.idToken = idToken
    }

    fun getIdToken(): String? {
        return idToken
    }
}