package com.example.e_kan.retrofit

import com.example.e_kan.retrofit.response.DefaultResponse
import com.example.e_kan.retrofit.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthService {
    //login
    @FormUrlEncoded
    @POST("auth_penjual/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<LoginResponse>

    //precheck email
    @FormUrlEncoded
    @POST("auth_penjual/precheckEmail")
    fun precheck(
        @Field("email") email: String
    ): Call<DefaultResponse>

    //register
    @FormUrlEncoded
    @POST("auth_penjual/register")
    fun register(
        @Field("nama") nama: String,
        @Field("nama_toko") nama_toko: String,
        @Field("alamat") alamat: String,
        @Field("email") email: String,
        @Field("nohp") nohp: String,
        @Field("password") password: String,
    ): Call<DefaultResponse>

    //request OTP
    @FormUrlEncoded
    @POST("auth_penjual/requestOtp")
    fun requestOtp(
        @Field("email") email: String
    ): Call<DefaultResponse>

    //verify OTP
    @FormUrlEncoded
    @POST("auth_penjual/verifyOtp")
    fun verifyOtp(
        @Field("email") email: String,
        @Field("otp") otp: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("auth_penjual/addToken")
    fun addToken(
        @Field("idpenjual") idpenjual: String,
        @Field("device_token") device_token: String,
    ): Call<DefaultResponse>

    //refresh auth token
    @FormUrlEncoded
    @POST("auth_penjual/refreshToken")
    fun refreshAuthToken(
        @Field("idpenjual") idpenjual: String
    ): Call<LoginResponse>

    //get Foto User
    @FormUrlEncoded
    @POST("auth_penjual/getUserFoto")
    fun getUserFoto(
        @Field("idpenjual") idpenjual: String
    ): Call<LoginResponse>
}