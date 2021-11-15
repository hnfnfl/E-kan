package com.example.e_kan.retrofit

import com.example.e_kan.retrofit.response.ProductResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface DataService {
    //get data semua produk
    @FormUrlEncoded
    @POST("main_penjual/getAllProduct")
    fun getAllProduct(
        @Field("idpenjual") idpenjual: String,
        @Header("Authorization") token: String
    ): Call<ProductResponse>

    //get top 5 penjualan
    @FormUrlEncoded
    @POST("main_penjual/getTopFive")
    fun getTopFiveProduct(
        @Field("idpenjual") idpenjual: String,
        @Header("Authorization") token: String
    ): Call<ProductResponse>

}