package com.fashionPeople.fashionGuide.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fashionPeople.fashionGuide.AppManager
import com.fashionPeople.fashionGuide.ClothingRepository
import com.fashionPeople.fashionGuide.data.Clothing
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
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

    @OptIn(DelicateCoroutinesApi::class)
    fun addClothing(imagePart: MultipartBody.Part) {
        // addClothing 함수 호출
        val uid = AppManager.getIdToken()
        val imageName = clothing.value!!.name
        GlobalScope.launch(Dispatchers.IO) {
            repository.addClothing(uid!!, imageName, imagePart)
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