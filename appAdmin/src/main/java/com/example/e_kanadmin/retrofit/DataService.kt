package com.example.e_kanadmin.retrofit

import com.example.e_kanadmin.retrofit.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface DataService {
    //edit profil admin
    @Multipart
    @POST("admin/main_admin/editProfile")
    fun editProfile(
        @Part("idadmin") idadmin: RequestBody,
        @Part("nama_admin") nama_admin: RequestBody,
        @Part("email_admin") email_admin: RequestBody,
        @Part("nohp_admin") nohp_admin: RequestBody,
        @Part filefoto: MultipartBody.Part? = null,
        @Header("Authorization") token: String
    ): Call<DefaultResponse>

    //get daftar transaksi
    @POST("admin/main_admin/getTransaction")
    fun getTransaction(
        @Header("Authorization") token: String
    ): Call<TransactionResponse>

    //edit status transaksi
    @FormUrlEncoded
    @POST("admin/main_admin/editStatus")
    fun editStatus(
        @Field("idpesanan") idpesanan: String,
        @Field("transaksi") transaksi: String,
        @Header("Authorization") token: String
    ): Call<DefaultResponse>

    //get semua user
    @POST("admin/main_admin/getAllUser")
    fun getAllUser(
        @Header("Authorization") token: String
    ): Call<UserResponse>

    //get semua penjual
    @POST("admin/main_admin/getAllSeller")
    fun getAllSeller(
        @Header("Authorization") token: String
    ): Call<SellerResponse>

    //get semua produk
    @POST("admin/main_admin/getAllProduct")
    fun getAllProduct(
        @Header("Authorization") token: String
    ): Call<ProductResponse>

    //edit status user
    @FormUrlEncoded
    @POST("admin/main_admin/editStatusUser")
    fun editStatusUser(
        @Field("iduser") iduser: String,
        @Field("status") status: String,
        @Header("Authorization") token: String
    ): Call<DefaultResponse>

    //edit status penjual
    @FormUrlEncoded
    @POST("admin/main_admin/editStatusSeller")
    fun editStatusSeller(
        @Field("idpenjual") idpenjual: String,
        @Field("status") status: String,
        @Header("Authorization") token: String
    ): Call<DefaultResponse>

    //edit status produk
    @FormUrlEncoded
    @POST("admin/main_admin/editStatusProduct")
    fun editStatusProduct(
        @Field("idproduk") idproduk: String,
        @Field("status") status: String,
        @Header("Authorization") token: String
    ): Call<DefaultResponse>
}