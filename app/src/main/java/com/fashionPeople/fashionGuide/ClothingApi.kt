package com.fashionPeople.fashionGuide

import com.fashionPeople.fashionGuide.data.Clothing
import com.fashionPeople.fashionGuide.data.Weather
import com.fashionPeople.fashionGuide.data.request.WeatherRequest
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

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

    @GET("weather")
    fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Call<Weather>

    @DELETE("closet/delete/{uid}/{cloth_id}")
    fun deleteClothing(
        @Path("uid") uid: String,
        @Path("cloth_id") id: String
    ): Call<Unit>


    //옷을 업데이트 하는 api
    @Multipart
    @DELETE("closet/{id}")
    fun updateClothing(
        @Path("id") id: String,
        @Part("uid") uid: String,
        @Part("imageName") imageName: String,
        @Part image: MultipartBody.Part
    ): Call<Void>
}