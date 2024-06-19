package com.fashionPeople.fashionGuide.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fashionPeople.fashionGuide.ClothingRepository
import com.fashionPeople.fashionGuide.data.Event
import com.fashionPeople.fashionGuide.data.EventList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class UserRegistrationViewModel @Inject constructor(
    private val repository: ClothingRepository
): ViewModel() {

    private val _closeActivityEvent = MutableLiveData<Event<Unit>>()
    val closeActivityEvent: LiveData<Event<Unit>> = _closeActivityEvent

    fun updateUserInfo(uid: String, ageGroup: String, gender: String) {
        viewModelScope.launch {
            repository.updateUserInfo(uid, ageGroup, gender)
        }
    }

    fun closeActivity() {
        _closeActivityEvent.value = Event(Unit)
    }
}