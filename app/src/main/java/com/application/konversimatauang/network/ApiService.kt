package com.application.konversimatauang.network

import com.application.konversimatauang.helper.EndPoint
import com.application.konversimatauang.model.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET(EndPoint.CONVERT_URL)
    suspend fun convertCurrency(@Query("api_key") access_key: String,
                                @Query("from") from: String,
                                @Query("to") to: String,
                                @Query("amount") amount: Double) : Response<ApiResponse>
}