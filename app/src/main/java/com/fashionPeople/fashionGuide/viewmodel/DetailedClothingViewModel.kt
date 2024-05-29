package com.fashionPeople.fashionGuide.viewmodel

import android.util.Log
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
    // 옷 삭제
    fun deleteClothing(){
        repository.deleteClothing(_clothing.value!!.id).observeForever {
            if(it.status == Status.SUCCESS){
                Log.d("deleteClothing", "success")
            }
            else{
                Log.d("deleteClothing", "${it.status}")
            }
        }
    }

}