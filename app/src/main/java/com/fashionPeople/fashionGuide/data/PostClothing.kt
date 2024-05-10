package com.fashionPeople.fashionGuide.data

import okhttp3.MultipartBody

data class PostClothing(
    val uid: String,
    var name: String,
    val image: MultipartBody
)