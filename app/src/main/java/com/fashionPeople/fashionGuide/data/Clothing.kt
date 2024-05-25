package com.fashionPeople.fashionGuide.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Clothing(
    val id: Int,
    var imageName: String,
    val imageUrl: String
) : Parcelable