package com.fashionPeople.fashionGuide

import com.fashionPeople.fashionGuide.data.Clothing
import com.fashionPeople.fashionGuide.data.UserInfo
import com.fashionPeople.fashionGuide.data.Weather
import com.fashionPeople.fashionGuide.data.response.RecommendedClothing
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ClothingApi {
    @Multipart
    @POST("clothes/add")
    fun addClothing(
        @Part("uid") uid: String,
        @Part("imageName") imageName: String,
        @Part image: MultipartBody.Part
    ): Call<Void>

    // 옷 불러오는 통신 api
    @GET("clothes/get/{uid}")
    fun getClothingList(@Path("uid") uid: String): Call<List<Clothing>>

    @GET("weather/get")
    fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Call<Weather>

    @DELETE("clothes/delete/{uid}/{cloth_id}")
    fun deleteClothing(
        @Path("uid") uid: String,
        @Path("cloth_id") id: String
    ): Call<Unit>

    @GET("user/info/get")
    fun getUserInfo(
        @Query("uid") uid: String
    ): Call<UserInfo>

    @FormUrlEncoded
    @POST("user/info/update")
    fun updateUserInfo(
        @Field("uid") uid: String,
        @Field("age") age: String,
        @Field("sex") sex: String
    ): Call<Void>

    @GET("clothes/propose")
    fun entireRecommendClothing(
        @Query("uid") uid: String,
        @Query("style") style: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("recommendFlag") flag: Int,
    ): Call<List<RecommendedClothing>>

    @GET("clothes/propose")
    fun partialRecommendClothing(
        @Query("uid") uid: String,
        @Query("style") style: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("clothId") clothId: String,
        @Query("recommendFlag") flag: Int,
    ): Call<RecommendedClothing>
}