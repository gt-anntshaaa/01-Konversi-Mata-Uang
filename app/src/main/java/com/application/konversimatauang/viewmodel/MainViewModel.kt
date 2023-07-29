package com.application.konversimatauang.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.konversimatauang.helper.Resource
import com.application.konversimatauang.helper.SingleLiveEvent
import com.application.konversimatauang.model.ApiResponse
import com.application.konversimatauang.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repo: MainRepository) : ViewModel() {
    // cached
    private val _data = SingleLiveEvent<Resource<ApiResponse>>()

    // public
    val data = _data
    val convertedRate = MutableLiveData<Double>()

    fun getConvertedData(access_key: String, from: String, to: String, amount: Double){
        viewModelScope.launch {
            repo.getConvertedData(access_key, from, to, amount).collect{
                _data.value = it
            }
        }
    }
}