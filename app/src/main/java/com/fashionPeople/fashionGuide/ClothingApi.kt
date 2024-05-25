package com.fashionPeople.fashionGuide

import com.fashionPeople.fashionGuide.data.Clothing
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
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

    // 옷 불러오는 통신 api
    @GET("clothes/{uid}")
    fun getClothingList(@Path("uid") uid: String): Call<List<Clothing>>

    @DELETE("clothes/{id}")
    fun deleteClothing(@Path("id") id: String): Call<Unit>
}