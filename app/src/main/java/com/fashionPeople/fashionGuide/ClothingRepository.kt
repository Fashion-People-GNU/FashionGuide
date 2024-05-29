package com.fashionPeople.fashionGuide

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fashionPeople.fashionGuide.data.Clothing
import com.fashionPeople.fashionGuide.data.Event
import com.fashionPeople.fashionGuide.data.Resource
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/**
 * Repository class for managing clothing data.
 *
 * @property api The ClothingApi instance for making network requests.
 */
open class ClothingRepository @Inject constructor(private val api: ClothingApi) {
    private val _event = MutableLiveData<Event<Unit>>()
    val event: LiveData<Event<Unit>> = _event

    /**
     * Adds a clothing item.
     *
     * @param uid The user ID.
     * @param imageName The name of the image.
     * @param image The image file.
     * @return LiveData containing the result of the operation.
     */
    open fun addClothing(uid: String, imageName: String, image: MultipartBody.Part): LiveData<Resource<Any?>> {
        val result = MutableLiveData<Resource<Any?>>()
        result.postValue(Resource.loading(null))  // 초기 로딩 상태 설정
        api.addClothing(uid, imageName, image).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    result.postValue(Resource.success(null))
                    _event.postValue(Event(Unit))
                } else {
                    result.postValue(Resource.error("Failed to add clothing", null))  // 실패 상태 포스트

                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                result.postValue(Resource.error("통신 오류", null))
            }
        })
        return result
    }
    // getClothingList() 함수를 통해 옷리스트를 통신을 통해 불러오는 코드
    open fun getClothingList(): LiveData<Resource<List<Clothing>>> {
        val result = MutableLiveData<Resource<List<Clothing>>>()
        api.getClothingList(AccountAssistant.getUID()!!).enqueue(object : Callback<List<Clothing>> {
            override fun onResponse(call: Call<List<Clothing>>, response: Response<List<Clothing>>) {
                if (response.isSuccessful) {
                    result.value = Resource.success(response.body()!!)
                } else {
                    result.value = Resource.error("Failed to get clothing list", null)
                }

            }

            override fun onFailure(call: Call<List<Clothing>>, t: Throwable) {
                result.value = Resource.error("Network error", null)
            }
        })
        return result
    }

    open fun deleteClothing(id: String): LiveData<Resource<Unit>> {
        val result = MutableLiveData<Resource<Unit>>()
        api.deleteClothing(id).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    result.value = Resource.success(Unit)
                } else {
                    result.value = Resource.error("Failed to delete clothing", null)
                    Log.d("test",response.message())
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                result.value = Resource.error("Network error", null)
            }
        })
        return result
    }

    //옷장 업데이트 api 호출
    open fun updateClothing(id: String, imageName: String, image: MultipartBody.Part): LiveData<Resource<Any?>> {
        val result = MutableLiveData<Resource<Any?>>()
        result.postValue(Resource.loading(null))  // 초기 로딩 상태 설정
        api.updateClothing(AccountAssistant.getUID()!!,id, imageName, image).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    result.postValue(Resource.success(null))
                } else {
                    result.postValue(Resource.error("Failed to update clothing", null))  // 실패 상태 포스트

                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                result.postValue(Resource.error("통신 오류", null))
            }
        })
        return result
    }
}
