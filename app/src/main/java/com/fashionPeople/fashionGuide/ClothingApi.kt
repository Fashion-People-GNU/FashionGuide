package com.fashionPeople.fashionGuide

import com.fashionPeople.fashionGuide.data.Clothing
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface ClothingApi {
    @POST("clothes")
    fun addClothing(@Body clothing: Clothing): Call<Clothing>

    @DELETE("clothes/{id}")
    fun deleteClothing(@Path("id") id: Int): Call<Unit>
}