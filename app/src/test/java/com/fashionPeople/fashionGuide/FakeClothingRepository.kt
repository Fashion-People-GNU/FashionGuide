package com.fashionPeople.fashionGuide

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fashionPeople.fashionGuide.data.Clothing
import com.fashionPeople.fashionGuide.data.Resource
import okhttp3.MultipartBody
import javax.inject.Inject

class FakeClothingRepository @Inject constructor(private val api: ClothingApi) : ClothingRepository(api) {

    private val clothingList = mutableListOf<Clothing>()
    private var shouldReturnError = false

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override fun addClothing(uid: String, imageName: String, image: MultipartBody.Part): LiveData<Resource<Any?>> {
        val result = MutableLiveData<Resource<Any?>>()
        if (shouldReturnError) {
            result.value = Resource.error("Test exception", null)
        } else {
            result.value = Resource.success(null)
        }
        return result
    }

    override fun getClothingList(): LiveData<Resource<List<Clothing>>> {
        val result = MutableLiveData<Resource<List<Clothing>>>()
        if (shouldReturnError) {
            result.value = Resource.error("Test exception", null)
        } else {
            result.value = Resource.success(clothingList)
        }
        return result
    }

    override fun deleteClothing(id: String): LiveData<Resource<Unit>> {
        val result = MutableLiveData<Resource<Unit>>()
        if (shouldReturnError) {
            result.value = Resource.error("Test exception", null)
        } else {
            clothingList.removeIf { it.id == id }
            result.value = Resource.success(Unit)
        }
        return result
    }
}
