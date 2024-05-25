package com.fashionPeople.fashionGuide.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fashionPeople.fashionGuide.ClothingRepository
import com.fashionPeople.fashionGuide.R
import com.fashionPeople.fashionGuide.data.Clothing
import com.fashionPeople.fashionGuide.data.Event
import com.fashionPeople.fashionGuide.data.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ClothingRepository
): ViewModel() {
    private val _clothingLiveData = MutableLiveData<List<Clothing>>()
    private val _bottomSheetOpen = mutableStateOf(false)
    private val _isTabScreen = mutableIntStateOf(0)
    val clothingLiveData:MutableLiveData<List<Clothing>>
        get() = _clothingLiveData

    val bottomSheetOpen: MutableState<Boolean>
        get() = _bottomSheetOpen

    val isTabScreen: MutableState<Int>
        get() = _isTabScreen

    fun setTabScreen(tab: Int){
        _isTabScreen.intValue = tab
    }

    fun getClothingList(){
        repository.getClothingList().observeForever { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    _clothingLiveData.value = resource.data
                    Log.d("test","tt")
                }
                Status.ERROR -> {
                    // 에러 처리, 예를 들어 사용자에게 메시지 표시
                    setCloth()
                    Log.d("test", "Error adding clothing: ${resource.message}")

                }
                Status.LOADING -> {

                }
            }

        }
    }
    fun setBottomSheet(isBottomSheet: Boolean){
        _bottomSheetOpen.value = isBottomSheet
    }

    fun deleteClothing(id: Int) {
        repository.deleteClothing(id).observeForever { resource ->
            if (resource.status == Status.SUCCESS) {
                _clothingLiveData.value = _clothingLiveData.value.orEmpty().filterNot { it.id == id }
            }
        }
    }

    fun setCloth(){
        val testList : List<Clothing> = listOf(Clothing(1,"테스트 옷","https://i.namu.wiki/i/8e4XLC6zL2-NsKYEutHbkCTPOTZehEKDsFBIIADszpYkLpvpmj8nQpOaRtmfYWFBr50rNovvVPyQB1TSD6Q0Rw.webp"))
        _clothingLiveData.value = testList
    }
}