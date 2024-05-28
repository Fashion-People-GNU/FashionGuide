package com.fashionPeople.fashionGuide

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class TestCoroutineRule : TestWatcher(), TestCoroutineScope by TestCoroutineScope() {

    @OptIn(ExperimentalStdlibApi::class)
    override fun starting(description: Description?) {
        Dispatchers.setMain(this.coroutineContext[CoroutineDispatcher.Key]!!)
    }

    override fun finished(description: Description?) {
        this.cleanupTestCoroutines()
        Dispatchers.resetMain()
    }
}