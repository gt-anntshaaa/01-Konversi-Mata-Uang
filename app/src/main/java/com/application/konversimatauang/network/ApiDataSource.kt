package com.application.konversimatauang.network

import javax.inject.Inject

class ApiDataSource @Inject constructor(private val apiService: ApiService) {
    suspend fun getConvertedRate(acces_key: String, from: String, to: String, amount: Double)
                = apiService.convertCurrency(acces_key, from, to, amount)
}