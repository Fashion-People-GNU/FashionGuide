package com.fashionPeople.fashionGuide.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fashionPeople.fashionGuide.ClothingRepository
import com.fashionPeople.fashionGuide.data.Clothing
import com.fashionPeople.fashionGuide.data.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ClothingRepository
): ViewModel() {
    private val clothingLiveData = MutableLiveData<List<Clothing>>()


    fun addClothing(clothing: Clothing) {
        repository.addClothing(clothing).observeForever { resource ->
            if (resource.status == Status.SUCCESS) {
                clothingLiveData.value = clothingLiveData.value.orEmpty() + clothing
            }
        }
    }

    fun deleteClothing(id: Int) {
        repository.deleteClothing(id).observeForever { resource ->
            if (resource.status == Status.SUCCESS) {
                clothingLiveData.value = clothingLiveData.value.orEmpty().filterNot { it.id == id }
            }
        }
    }
}