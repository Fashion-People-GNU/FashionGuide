package com.fashionPeople.fashionGuide

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ClothingApi {
    @Multipart
    @POST("upload")
    fun addClothing(
        @Part("uid") uid: String,
        @Part("imageName") imageName: String,
        @Part image: MultipartBody.Part
    ): Call<Void>

    @DELETE("clothes/{id}")
    fun deleteClothing(@Path("id") id: Int): Call<Unit>
}