package com.fashionPeople.fashionGuide.viewmodel


import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fashionPeople.fashionGuide.ClothingRepository
import com.fashionPeople.fashionGuide.data.Clothing

import com.fashionPeople.fashionGuide.data.Status

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class RequestClothingViewModel @Inject constructor(
    private val repository: ClothingRepository
): ViewModel() {

    private val _clothingLiveData = MutableLiveData<List<Clothing>?>()
    private val _isSubmitDialogScreen = mutableIntStateOf(0)

    val clothingLiveData: MutableLiveData<List<Clothing>?>
        get() = _clothingLiveData

    val isSubmitDialogScreen: MutableState<Int>
        get() = _isSubmitDialogScreen

    init {
        getClothingList()
        Log.d("test","request:" +repository.hashCode().toString())
    }

    fun setSubmitDialogScreen(value: Int){
        _isSubmitDialogScreen.value = value
    }


    private fun getClothingList(){
        repository.getClothingList().observeForever { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    _clothingLiveData.postValue(resource.data)
                    Log.d("test","equest 데이터 가져옴")
                }
                Status.ERROR -> {
                    // 에러 처리, 예를 들어 사용자에게 메시지 표시
                    _clothingLiveData.postValue(listOf())
                    Log.d("test", "Error adding clothing: ${resource.message}")

                }
                Status.LOADING -> {
                }
            }

        }
    }
}