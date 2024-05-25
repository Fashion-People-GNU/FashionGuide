package com.fashionPeople.fashionGuide.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Clothing(
    val id: String,
    var imageName: String,
    val imageUrl: String
) : Parcelable