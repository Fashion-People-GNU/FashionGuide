package com.fashionPeople.fashionGuide

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fashionPeople.fashionGuide.data.Clothing
import com.fashionPeople.fashionGuide.data.Resource
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ClothingRepository @Inject constructor(private val api: ClothingApi) {
    fun addClothing(uid: String, imageName: String, image: MultipartBody.Part) {

        api.addClothing(uid, imageName, image).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Resource.success(null)
                    Log.d("test", "성공?")
                } else {
                    Resource.error("Failed to add clothing", null)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Resource.error("Network error", null)
            }
        })

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
