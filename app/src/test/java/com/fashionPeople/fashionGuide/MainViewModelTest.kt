package com.fashionPeople.fashionGuide

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.fashionPeople.fashionGuide.data.Clothing
import com.fashionPeople.fashionGuide.data.Resource
import com.fashionPeople.fashionGuide.viewmodel.MainViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import javax.inject.Inject

@HiltAndroidTest
@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = TestCoroutineRule()

    @Mock
    private lateinit var clothingObserver: Observer<List<Clothing>?>

    @Inject
    lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        hiltRule.inject()
        viewModel.clothingLiveData.observeForever(clothingObserver)
    }

    @Test
    fun getClothingList_success() = coroutinesTestRule.runBlockingTest {
        val clothingList = listOf(Clothing("1", "Test Clothing", "https://test.url"))

        viewModel.getClothingList()

        Mockito.verify(clothingObserver).onChanged(clothingList)
    }

    @Test
    fun getClothingList_error() = coroutinesTestRule.runBlockingTest {

        viewModel.getClothingList()

        Mockito.verify(clothingObserver).onChanged(null)
    }

    @Test
    fun deleteClothing_success() = coroutinesTestRule.runBlockingTest {
        val id = "1"

        viewModel.deleteClothing(id)

        Mockito.verify(clothingObserver).onChanged(listOf())
    }

    @Test
    fun deleteClothing_error() = coroutinesTestRule.runBlockingTest {
        val id = "1"

        viewModel.deleteClothing(id)

        Mockito.verify(clothingObserver).onChanged(null)
    }
}