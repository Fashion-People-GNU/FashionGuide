package com.fashionPeople.fashionGuide.data.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecommendedClothing(
    val id: String,
    var imageName: String,
    val imageUrl: String,
    val type: String,
    val style: String,
    val length: String,
    val printing: String,
    val color: String,
    val material: String
) : Parcelable