package com.fashionPeople.fashionGuide

// NetworkModule.kt
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS) // 15초 이상 연결이 안되면 타임아웃
        .readTimeout(15, TimeUnit.SECONDS) // 15초 이상 읽기가 안되면 타임아웃
        .writeTimeout(15, TimeUnit.SECONDS) // 15초 이상 쓰기가 안되면 타임아웃
        .build()

    @Provides
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl("http://203.232.193.151:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Provides
    fun provideClothingApi(retrofit: Retrofit): ClothingApi =
        retrofit.create(ClothingApi::class.java)

}
