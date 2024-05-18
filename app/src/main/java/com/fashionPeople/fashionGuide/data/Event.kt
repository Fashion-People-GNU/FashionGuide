package com.fashionPeople.fashionGuide.data

open class Event<out T>(private val content: T) {
    var hasBeenHandled = false
        private set // 외부에서 set을 할 수 없도록

    fun getContentIfNotHandled(): T? {
        return if (!hasBeenHandled) {
            hasBeenHandled = true
            content
        } else null
    }

    fun peekContent(): T = content
}
