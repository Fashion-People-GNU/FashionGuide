package com.fashionPeople.fashionGuide

// NetworkModule.kt
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl("http://203.232.193.151:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    fun provideClothingApi(retrofit: Retrofit): ClothingApi =
        retrofit.create(ClothingApi::class.java)

}
