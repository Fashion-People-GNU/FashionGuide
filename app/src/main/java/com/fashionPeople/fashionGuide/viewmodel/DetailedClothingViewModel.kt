package com.fashionPeople.fashionGuide.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fashionPeople.fashionGuide.ClothingRepository
import com.fashionPeople.fashionGuide.R
import com.fashionPeople.fashionGuide.data.Clothing
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailedClothingViewModel @Inject constructor(
    private val repository: ClothingRepository
): ViewModel() {
    private val _clothing = MutableLiveData<Clothing>()

    val clothing :MutableLiveData<Clothing>
        get() = _clothing

    fun setCloth(){
        _clothing.value = Clothing(id =1,"테스트 옷", R.drawable.test_item)
    }
}