package com.application.konversimatauang.network

import android.util.Log
import com.application.konversimatauang.helper.Resource
import retrofit2.Response

abstract class BaseDataSource{
    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Resource<T>{
        try {
            val response = apiCall()
            if (response.isSuccessful){
                val body = response.body()
                if (body != null){
                    return Resource.Success(body)
                }
            }
            return error("${response.code()} ${response.message()}")
        }catch (e: Exception){
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): Resource<T>{
        Log.e("remoteDataSource", "error: $message", )
        return Resource.Error("Network call has failed for a following reason: $message")
    }
}
