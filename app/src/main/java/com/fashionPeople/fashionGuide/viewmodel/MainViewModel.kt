package com.fashionPeople.fashionGuide.viewmodel

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fashionPeople.fashionGuide.AccountAssistant
import com.fashionPeople.fashionGuide.ClothingRepository
import com.fashionPeople.fashionGuide.data.Clothing
import com.fashionPeople.fashionGuide.data.Event
import com.fashionPeople.fashionGuide.data.EventList
import com.fashionPeople.fashionGuide.data.Status
import com.fashionPeople.fashionGuide.data.TodayDate
import com.fashionPeople.fashionGuide.data.Weather
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ClothingRepository
): ViewModel() {

    private val _event = repository.event

    private val _clothingLiveData = MutableLiveData<List<Clothing>?>()
    private val _bottomSheetOpen = mutableStateOf(false)
    private val _isTabScreen = mutableIntStateOf(0)
    private val _isRegionDialogScreen = mutableIntStateOf(0)
    private val _isResultDialogScreen = mutableIntStateOf(0)
    private val _isSourcesDialogScreen = mutableIntStateOf(0)
    private val _todayDate = mutableStateOf(TodayDate(0,0,0,"없음"))
    private val _weather = mutableStateOf(Weather( 0.0, 0.0, 0.0, 0.0, "없음","없음",0.0))
    private val _region = mutableStateOf("없음")

    private val _isLoading = mutableStateOf(false)

    val isLoading : MutableState<Boolean>
        get() = _isLoading
    val clothingLiveData: MutableLiveData<List<Clothing>?>
        get() = _clothingLiveData

    val event : LiveData<Event<EventList>>
        get() = _event

    val region: MutableState<String>
        get() = _region

    val bottomSheetOpen: MutableState<Boolean>
        get() = _bottomSheetOpen

    val isTabScreen: MutableState<Int>
        get() = _isTabScreen

    val isRegionDialogScreen: MutableState<Int>
        get() = _isRegionDialogScreen

    val isSourcesDialogScreen: MutableState<Int>
        get() = _isSourcesDialogScreen

    val isResultDialogScreen: MutableState<Int>
        get() = _isResultDialogScreen

    val weather: MutableState<Weather>
        get() = _weather

    val todayDate: MutableState<TodayDate>
        get() = _todayDate


    fun setTabScreen(tab: Int){
        _isTabScreen.intValue = tab
    }

    init {
        getClothingList()
        Log.d("test",repository.hashCode().toString())
    }
    fun init(){
        getDate()
        region.value = AccountAssistant.getData("region") ?: "없음"
        getWeather()
    }

    private fun getWeather(){
        if (AccountAssistant.getData("lat") == null || AccountAssistant.getData("lon") == null) return
        repository.getWeather(AccountAssistant.getData("lat")!!.toDouble(), AccountAssistant.getData("lon")!!.toDouble()).observeForever { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    _weather.value = resource.data!!
                    Log.d("test","날씨 가져옴")
                }
                Status.ERROR -> {
                    // 에러 처리, 예를 들어 사용자에게 메시지 표시
                    Log.d("test", "Error 날씨 ${resource.message}")
                }
                Status.LOADING -> {

                }
            }

        }
    }


    private fun getDate(){
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1 // 월은 0부터 시작하므로 1을 더해줌
        val year = calendar.get(Calendar.YEAR)
        val date = TodayDate(year, month, day, calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())?: "없음")
        _todayDate.value = date
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun getRegion(activity: Activity){
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),0)
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                getCityName(it.latitude, it.longitude,activity)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun getCityName(latitude: Double, longitude: Double, context: Context) {
        val geocoder = Geocoder(context, Locale.getDefault())
        geocoder.getFromLocation(latitude, longitude, 1) { addresses ->
            if (addresses.isNotEmpty()) {
                val cityName = addresses[0].locality
                region.value = cityName
                AccountAssistant.setData("region", cityName)
                AccountAssistant.setData("lat", latitude.toString())
                AccountAssistant.setData("lon", longitude.toString())
            }
        }

    }

    fun setRegionDialogScreen(dialog: Int){
        _isRegionDialogScreen.intValue = dialog
    }

    fun setResultDialogScreen(dialog: Int){
        _isResultDialogScreen.intValue = dialog
    }

    fun setSourcesDialogScreen(dialog: Int){
        _isSourcesDialogScreen.intValue = dialog
    }

    fun getClothingList(){
        repository.getClothingList().observeForever { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    _clothingLiveData.postValue(resource.data)
                    isLoading.value = false
                    Log.d("test","데이터 가져옴")
                }
                Status.ERROR -> {
                    isLoading.value = false
                    // 에러 처리, 예를 들어 사용자에게 메시지 표시
                    _clothingLiveData.postValue(listOf())
                    Log.d("test", "Error adding clothing: ${resource.message}")

                }
                Status.LOADING -> {
                    isLoading.value = true
                }
            }

        }
    }
    fun setBottomSheet(isBottomSheet: Boolean){
        _bottomSheetOpen.value = isBottomSheet
    }

    fun deleteClothing(id: String) {
        viewModelScope.launch {
            repository.deleteClothing(AccountAssistant.getUID()!!,id).observeForever { resource ->
                if (resource.status == Status.SUCCESS) {
                    _clothingLiveData.value = _clothingLiveData.value.orEmpty().filterNot { it.id == id }
                }
            }
        }



    }

    fun setCloth(){
        val testList : List<Clothing> = listOf(Clothing("","테스트 옷","https://i.namu.wiki/i/8e4XLC6zL2-NsKYEutHbkCTPOTZehEKDsFBIIADszpYkLpvpmj8nQpOaRtmfYWFBr50rNovvVPyQB1TSD6Q0Rw.webp"))
        _clothingLiveData.value = testList
    }
}