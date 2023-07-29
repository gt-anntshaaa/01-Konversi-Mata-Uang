package com.application.konversimatauang.helper

sealed class Resource <out T>{
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val message: String) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}

//data class Resource<out T>(val status: Status, val data: T?, val message: String?){
//    enum class Status{
//        SUCCESS,
//        LOADING,
//        ERROR
//    }
//
//    companion object{
//        fun<T> success(data: T): Resource<T>{
//            return Resource(Status.SUCCESS, data, null)
//        }
//
//        fun<T> loading(data: T? = null): Resource<T>{
//            return Resource(Status.LOADING, data, null)
//        }
//
//        fun<T> error(message: String, data: T? = null): Resource<T>{
//            return Resource(Status.ERROR, data, message)
//        }
//    }
//}
