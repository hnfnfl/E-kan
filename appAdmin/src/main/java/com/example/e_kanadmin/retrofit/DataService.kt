package com.example.e_kanadmin.retrofit

import com.example.e_kanadmin.retrofit.response.DefaultResponse
import com.example.e_kanadmin.retrofit.response.TransactionResponse
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

    //get list notifikasi
//    @FormUrlEncoded
//    @POST("penjual/main_penjual/getNotification")
//    fun getNotification(
//        @Field("idpenjual") idpenjual: String,
//        @Header("Authorization") token: String
//    ): Call<NotificationResponse>

}