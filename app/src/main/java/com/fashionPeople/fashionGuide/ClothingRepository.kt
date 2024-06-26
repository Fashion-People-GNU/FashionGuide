package com.fashionPeople.fashionGuide

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fashionPeople.fashionGuide.data.Clothing
import com.fashionPeople.fashionGuide.data.Event
import com.fashionPeople.fashionGuide.data.EventList
import com.fashionPeople.fashionGuide.data.Resource
import com.fashionPeople.fashionGuide.data.UserInfo
import com.fashionPeople.fashionGuide.data.Weather
import com.fashionPeople.fashionGuide.data.response.RecommendedClothing
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository class for managing clothing data.
 *
 * @property api The ClothingApi instance for making network requests.
 */
@Singleton
open class ClothingRepository @Inject constructor(private val api: ClothingApi) {
    private val _event = MutableLiveData<Event<EventList>>()
    val event: MutableLiveData<Event<EventList>> get() = _event

    fun setEvent(event: EventList) {
        _event.value = Event(event)
        Log.d("test","set${event}")
    }

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
                } else {
                    Log.d("test",response.toString())
                    result.postValue(Resource.error("옷 사진이 아닙니다", null))  // 실패 상태 포스트

                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                result.postValue(Resource.error("통신 오류", null))
                Log.d("test",t.toString())
            }
        })
        return result
    }
    // getClothingList() 함수를 통해 옷리스트를 통신을 통해 불러오는 코드
    open fun getClothingList(): LiveData<Resource<List<Clothing>>> {
        val result = MutableLiveData<Resource<List<Clothing>>>()
        result.postValue(Resource.loading(null))  // 초기 로딩 상태 설정
        api.getClothingList(AccountAssistant.getUID()!!).enqueue(object : Callback<List<Clothing>> {
            override fun onResponse(call: Call<List<Clothing>>, response: Response<List<Clothing>>) {
                if (response.isSuccessful) {
                    result.value = Resource.success(response.body()!!)
                } else {
                    Log.d("test",response.toString())
                    result.value = Resource.error("Failed to get clothing list", null)
                }

            }

            override fun onFailure(call: Call<List<Clothing>>, t: Throwable) {
                result.value = Resource.error("Network error", null)
            }
        })
        return result
    }

    open fun deleteClothing(uid: String,id: String): LiveData<Resource<Unit>> {
        val result = MutableLiveData<Resource<Unit>>()
        result.postValue(Resource.loading(null))  // 초기 로딩 상태 설정
        api.deleteClothing(uid,id).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    result.value = Resource.success(Unit)
                    Log.d("test",response.toString())
                } else {
                    result.value = Resource.error("Failed to delete clothing", null)
                    Log.d("test",response.toString())
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

    open fun getWeather(lat: Double, lon: Double): LiveData<Resource<Weather>> {
        val result = MutableLiveData<Resource<Weather>>()
        result.postValue(Resource.loading(null))  // 초기 로딩 상태 설정
        api.getWeather(lat,lon).enqueue(object : Callback<Weather> {
            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                if (response.isSuccessful) {
                    result.value = Resource.success(response.body()!!)
                } else {
                    result.value = Resource.error("Failed to get weather", null)
                    Log.d("test",response.toString())
                }
            }

            override fun onFailure(call: Call<Weather>, t: Throwable) {
                result.value = Resource.error("Network error", null)
                Log.d("test",t.toString())
            }
        })
        return result
    }

    open fun getUserInfo(uid: String): LiveData<Resource<UserInfo>> {
        val result = MutableLiveData<Resource<UserInfo>>()
        result.postValue(Resource.loading(null))  // 초기 로딩 상태 설정
        api.getUserInfo(uid).enqueue(object : Callback<UserInfo> {
            override fun onResponse(call: Call<UserInfo>, response: Response<UserInfo>) {
                if (response.isSuccessful) {
                    result.value = Resource.success(response.body()!!)
                } else {
                    result.value = Resource.error("Failed to get user info", null)
                    Log.d("test",response.toString())
                }
            }

            override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                result.value = Resource.error("Network error", null)
                Log.d("test",t.toString())
            }
        })
        return result
    }

    open fun updateUserInfo(uid: String, age: String, sex: String): LiveData<Resource<Any?>> {
        val result = MutableLiveData<Resource<Any?>>()
        result.postValue(Resource.loading(null))  // 초기 로딩 상태 설정
        api.updateUserInfo(uid,age,sex).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    result.value = Resource.success(null)
                } else {
                    result.value = Resource.error("Failed to update user info", null)
                    Log.d("test",response.toString())
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                result.value = Resource.error("Network error", null)
                Log.d("test",t.toString())
            }
        })
        return result
    }

    open fun entireClothingList(uid: String ,style:String,lat: Double, lon: Double): MutableLiveData<Resource<List<RecommendedClothing>>> {
        val result = MutableLiveData<Resource<List<RecommendedClothing>>>()
        result.postValue(Resource.loading(null))  // 초기 로딩 상태 설정
        api.entireRecommendClothing(uid, style,lat,lon,0).enqueue(object : Callback<List<RecommendedClothing>> {

            override fun onResponse(
                call: Call<List<RecommendedClothing>>,
                response: Response<List<RecommendedClothing>>) {
                if (response.isSuccessful) {
                    result.value = Resource.success(response.body()!!)
                } else {
                    result.value = Resource.error("Failed to get clothing list", null)
                    Log.d("test",response.toString())
                }
            }

            override fun onFailure(call: Call<List<RecommendedClothing>>, t: Throwable) {
                result.value = Resource.error("Network error", null)
                Log.d("test",t.toString())
            }


        })
        return result
    }

    open fun partialClothingList(uid: String ,style:String,lat: Double, lon: Double,clothingId: String): MutableLiveData<Resource<RecommendedClothing>> {
        val result = MutableLiveData<Resource<RecommendedClothing>>()
        result.postValue(Resource.loading(null))  // 초기 로딩 상태 설정
        api.partialRecommendClothing(uid, style,lat,lon,clothingId,1).enqueue(object : Callback<RecommendedClothing> {

            override fun onResponse(call: Call<RecommendedClothing>, response: Response<RecommendedClothing>) {
                if (response.isSuccessful) {
                    result.value = Resource.success(response.body()!!)
                } else {
                    result.value = Resource.error("Failed to get clothing list", null)
                    Log.d("test",response.toString())
                }
            }

            override fun onFailure(call: Call<RecommendedClothing>, t: Throwable) {
                result.value = Resource.error("Network error", null)
                Log.d("test",t.toString())
            }
        })
        return result
    }
}
