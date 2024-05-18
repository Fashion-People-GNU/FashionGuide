package com.fashionPeople.fashionGuide.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fashionPeople.fashionGuide.AccountAssistant
import com.fashionPeople.fashionGuide.ClothingRepository
import com.fashionPeople.fashionGuide.data.Clothing
import com.fashionPeople.fashionGuide.data.Event
import com.fashionPeople.fashionGuide.data.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class AddClothingViewModel @Inject constructor(
    private val repository: ClothingRepository
): ViewModel() {
    private val _clothing = MutableLiveData<Clothing>()
    private val _clothingUri = MutableLiveData<Uri>()

    private val _closeActivityEvent = MutableLiveData<Event<Unit>>()
    val closeActivityEvent: LiveData<Event<Unit>> = _closeActivityEvent

    val clothing : MutableLiveData<Clothing>
        get() = _clothing

    val clothingUri : MutableLiveData<Uri>
        get() = _clothingUri

    fun init(){
        val initClothing = Clothing(0,"",0)
        clothing.value = initClothing
    }
    fun setClothingName(name: String){
        clothing.value?.name = name
    }
    fun setClothingUri(uri: Uri){
        clothingUri.value = uri
    }

    fun addClothing(imagePart: MultipartBody.Part) {
        viewModelScope.launch {
            val uid = AccountAssistant.getUID() ?: return@launch  // UID가 null이면 함수를 종료
            val clothingName = clothing.value?.name ?: return@launch  // clothing의 이름이 null이면 함수를 종료

            Log.d("test2", clothingName)  // 로그 출력
            repository.addClothing(uid, clothingName, imagePart).observeForever { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        _closeActivityEvent.postValue(Event(Unit))  // 성공시 액티비티 종료 이벤트
                        Log.d("test","tt")
                    }
                    Status.ERROR -> {
                        // 에러 처리, 예를 들어 사용자에게 메시지 표시
                        Log.d("AddClothingVM", "Error adding clothing: ${resource.message}")
                    }
                    Status.LOADING -> {
                        // 로딩 상태 처리, 필요시 UI 업데이트
                    }
                }
            }
        }
    }

    fun createImagePartFromUri(context: Context, uri: Uri): MultipartBody.Part {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val outputFile = File(context.cacheDir, "image_file.jpg") // 이미지 파일의 저장 위치 설정

        inputStream?.use { input ->
            FileOutputStream(outputFile).use { output ->
                input.copyTo(output) // 입력 스트림을 출력 스트림으로 복사하여 이미지 파일 생성
            }
        }

        val imageRequestBody = outputFile.asRequestBody("image/*".toMediaTypeOrNull())

        return MultipartBody.Part.createFormData("image", outputFile.name, imageRequestBody)
    }
}