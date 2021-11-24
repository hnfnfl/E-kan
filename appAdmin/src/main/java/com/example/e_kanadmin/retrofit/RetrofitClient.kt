package com.example.e_kanadmin.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    fun apiRequest(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://ekan.icaapps.online") //ganti dengan domain API
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}