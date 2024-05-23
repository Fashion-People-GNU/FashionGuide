package com.fashionPeople.fashionGuide.viewmodel

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
            if (resource.status == Status.SUCCESS) {
                clothingLiveData.value = resource.data
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
        val testList : List<Clothing> = listOf(Clothing(1,"테스트 옷",R.drawable.content_image))
        _clothingLiveData.value = testList
    }
}