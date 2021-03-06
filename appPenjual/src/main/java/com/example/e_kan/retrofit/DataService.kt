package com.example.e_kan.retrofit

import com.example.e_kan.retrofit.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface DataService {
    //edit profil penjual
    @Multipart
    @POST("penjual/main_penjual/editProfile")
    fun editProfile(
        @Part("idpenjual") idpenjual: RequestBody,
        @Part("nama_penjual") nama_penjual: RequestBody,
        @Part("nama_toko") nama_toko: RequestBody,
        @Part("alamat_penjual") alamat_penjual: RequestBody,
        @Part("email_penjual") email_penjual: RequestBody,
        @Part("nohp_penjual") nohp_penjual: RequestBody,
        @Part filefoto: MultipartBody.Part? = null,
        @Header("Authorization") token: String
    ): Call<DefaultResponse>

    //get data semua produk
    @FormUrlEncoded
    @POST("penjual/main_penjual/getAllProduct")
    fun getAllProduct(
        @Field("idpenjual") idpenjual: String,
        @Header("Authorization") token: String
    ): Call<ProductResponse>

    //get top 5 penjualan
    @FormUrlEncoded
    @POST("penjual/main_penjual/getTopFive")
    fun getTopFiveProduct(
        @Field("idpenjual") idpenjual: String,
        @Header("Authorization") token: String
    ): Call<ProductResponse>

    //add data product
    @Multipart
    @POST("penjual/main_penjual/addProduct")
    fun addProduct(
        @Part("idpenjual") idpenjual: RequestBody,
        @Part("nama_produk") nama_produk: RequestBody,
        @Part("keterangan") keterangan: RequestBody,
        @Part("harga") harga: RequestBody,
        @Part("berat") berat: RequestBody,
        @Part("stok") stok: RequestBody,
        @Part filefoto: MultipartBody.Part? = null,
        @Header("Authorization") token: String
    ): Call<DefaultResponse>

    //edit data produk
    @Multipart
    @POST("penjual/main_penjual/editProduct")
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

    //delete produk
    @FormUrlEncoded
    @POST("penjual/main_penjual/deleteProduct")
    fun deleteProduct(
        @Field("idproduk") idproduk: String,
        @Header("Authorization") token: String
    ): Call<DefaultResponse>

    //get daftar pesanan
    @FormUrlEncoded
    @POST("penjual/main_penjual/getOrder")
    fun getOrder(
        @Field("idpenjual") idpenjual: String,
        @Header("Authorization") token: String
    ): Call<OrderResponse>

    //edit status pesanan
    @FormUrlEncoded
    @POST("penjual/main_penjual/editStatus")
    fun editStatus(
        @Field("idpesanan") idpesanan: String,
        @Field("status") status: String,
        @Header("Authorization") token: String
    ): Call<DefaultResponse>

    //get list notifikasi
    @FormUrlEncoded
    @POST("penjual/main_penjual/getNotification")
    fun getNotification(
        @Field("idpenjual") idpenjual: String,
        @Header("Authorization") token: String
    ): Call<NotificationResponse>

    //get Time Chart
    @FormUrlEncoded
    @POST("penjual/main_penjual/getTimeChart")
    fun getTimeChart(
        @Field("idpenjual") idpenjual: String,
        @Field("time") time: String,
        @Header("Authorization") token: String
    ): Call<ChartResponse>
}