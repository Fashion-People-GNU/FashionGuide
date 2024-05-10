package com.fashionPeople.fashionGuide.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Clothing(
    val id: Int,
    var name: String,
    val image: Int
) : Parcelable