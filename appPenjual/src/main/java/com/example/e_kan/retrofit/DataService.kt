package com.example.e_kan.retrofit

import com.example.e_kan.retrofit.response.DefaultResponse
import com.example.e_kan.retrofit.response.ProductResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

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



    //edit data produk
    @Multipart
    @POST("main_penjual/editProduct")
    fun editProduct(
        @Part("idproduk") idproduk: RequestBody,
        @Part("idpenjual") idpenjual: RequestBody,
        @Part("nama_produk") nama_produk: RequestBody,
        @Part("keterangan") keterangan: RequestBody,
        @Part("harga") harga: RequestBody,
        @Part("berat") berat: RequestBody,
        @Part("stok") stok: RequestBody,
        @Part filefoto: MultipartBody.Part? = null,
        @Header("Authorization") token: String
    ): Call<DefaultResponse>
}