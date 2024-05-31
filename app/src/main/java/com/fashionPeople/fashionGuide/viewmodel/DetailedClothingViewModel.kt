package com.fashionPeople.fashionGuide.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fashionPeople.fashionGuide.AccountAssistant
import com.fashionPeople.fashionGuide.ClothingRepository
import com.fashionPeople.fashionGuide.R
import com.fashionPeople.fashionGuide.data.Clothing
import com.fashionPeople.fashionGuide.data.Event
import com.fashionPeople.fashionGuide.data.EventList
import com.fashionPeople.fashionGuide.data.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailedClothingViewModel @Inject constructor(
    private val repository: ClothingRepository
): ViewModel() {
    private val _clothing = MutableLiveData<Clothing>()
    private val _isLoading = mutableStateOf(false)
    private val _message = MutableLiveData<Event<String>>()

    val message: MutableLiveData<Event<String>>
        get() = _message
    val clothing :MutableLiveData<Clothing>
        get() = _clothing

    val isLoading :MutableState<Boolean>
        get() = _isLoading

    fun setCloth(clothing: Clothing){
        _clothing.value = clothing
    }
    // 옷 삭제
    fun deleteClothing(){
        viewModelScope.launch {
            repository.deleteClothing(AccountAssistant.getUID()!!,_clothing.value!!.id).observeForever {
                resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        _message.postValue(Event(resource.message ?: "삭제 성공"))
                        Log.d("test", "success")
                        _isLoading.value = false
                        repository.setEvent(EventList.DELETE)
                    }
                    Status.ERROR -> {
                        _isLoading.value = false
                        Log.d("deleteClothing", "${resource.message}")
                    }
                    Status.LOADING -> {
                        _isLoading.value = true
                        Log.d("deleteClothing", "loading")
                    }
                }
            }
        }

    }

}