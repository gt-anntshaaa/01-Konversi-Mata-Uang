package com.application.konversimatauang.model

data class ApiResponse(
    val status: String,
    val update_date: String,
    val base_currency_code: String,
    val amount: String,
    val base_currency_name: String,
    val rates: HashMap<String, Rates> = HashMap()
)
