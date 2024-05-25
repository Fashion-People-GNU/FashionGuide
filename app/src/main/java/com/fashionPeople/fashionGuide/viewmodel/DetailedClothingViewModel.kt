package com.fashionPeople.fashionGuide.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fashionPeople.fashionGuide.ClothingRepository
import com.fashionPeople.fashionGuide.R
import com.fashionPeople.fashionGuide.data.Clothing
import com.fashionPeople.fashionGuide.data.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailedClothingViewModel @Inject constructor(
    private val repository: ClothingRepository
): ViewModel() {
    private val _clothing = MutableLiveData<Clothing>()

    val clothing :MutableLiveData<Clothing>
        get() = _clothing

    fun setCloth(clothing: Clothing){
        _clothing.value = clothing
    }
    // getClothingList() 함수를 통해 옷리스트를 통신을 통해 불러오는 코드

}