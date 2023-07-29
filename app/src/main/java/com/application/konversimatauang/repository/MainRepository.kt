package com.application.konversimatauang.repository

import com.application.konversimatauang.helper.Resource
import com.application.konversimatauang.model.ApiResponse
import com.application.konversimatauang.network.ApiDataSource
import com.application.konversimatauang.network.BaseDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiDataSource: ApiDataSource) : BaseDataSource() {
    suspend fun getConvertedData(access_key: String, from: String, to: String, amount: Double) : Flow<Resource<ApiResponse>>{
        return flow {
            emit(safeApiCall { apiDataSource.getConvertedRate(access_key, from, to, amount) })
        }.flowOn(Dispatchers.IO)
    }
}