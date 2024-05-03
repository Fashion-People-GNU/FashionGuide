package com.fashionPeople.fashionGuide

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fashionPeople.fashionGuide.data.Clothing
import com.fashionPeople.fashionGuide.data.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ClothingRepository @Inject constructor(private val api: ClothingApi) {
    fun addClothing(clothing: Clothing): LiveData<Resource<Clothing>> {
        val result = MutableLiveData<Resource<Clothing>>()
        api.addClothing(clothing).enqueue(object : Callback<Clothing> {
            override fun onResponse(call: Call<Clothing>, response: Response<Clothing>) {
                if (response.isSuccessful) {
                    result.value = Resource.success(response.body()!!)
                } else {
                    result.value = Resource.error("Failed to add clothing", null)
                }
            }

            override fun onFailure(call: Call<Clothing>, t: Throwable) {
                result.value = Resource.error("Network error", null)
            }
        })
        return result
    }

    fun deleteClothing(id: Int): LiveData<Resource<Unit>> {
        val result = MutableLiveData<Resource<Unit>>()
        api.deleteClothing(id).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    result.value = Resource.success(Unit)
                } else {
                    result.value = Resource.error("Failed to delete clothing", null)
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                result.value = Resource.error("Network error", null)
            }
        })
        return result
    }
}
