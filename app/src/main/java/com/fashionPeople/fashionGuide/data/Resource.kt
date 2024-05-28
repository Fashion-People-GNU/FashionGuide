package com.fashionPeople.fashionGuide.data

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T): Resource<T> = Resource(Status.SUCCESS, data, null)

        fun <T> error(msg: String, data: T?): Resource<T> = Resource(Status.ERROR, data, msg)

        fun <T> loading(data: T?): Resource<T> = Resource(Status.LOADING, data, null)
        //Resource 클래스는 서버로부터 받아온 데이터를 처리하기 위한 클래스입니다.
    }
}