package com.example.e_kanadmin.retrofit

import com.example.e_kanadmin.retrofit.response.DefaultResponse
import com.example.e_kanadmin.retrofit.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthService {
    //login
    @FormUrlEncoded
    @POST("admin/auth_admin/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<LoginResponse>

    //precheck email
    @FormUrlEncoded
    @POST("admin/auth_admin/precheckEmail")
    fun precheck(
        @Field("email") email: String
    ): Call<DefaultResponse>

    //register
    @FormUrlEncoded
    @POST("admin/auth_admin/register")
    fun register(
        @Field("nama") nama: String,
        @Field("email") email: String,
        @Field("nohp") nohp: String,
        @Field("password") password: String,
    ): Call<DefaultResponse>

    //request OTP
    @FormUrlEncoded
    @POST("admin/auth_admin/requestOtp")
    fun requestOtp(
        @Field("email") email: String
    ): Call<DefaultResponse>

    //verify OTP
    @FormUrlEncoded
    @POST("admin/auth_admin/verifyOtp")
    fun verifyOtp(
        @Field("email") email: String,
        @Field("otp") otp: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("admin/auth_admin/addToken")
    fun addToken(
        @Field("iadmin") iadmin: String,
        @Field("device_token") device_token: String,
    ): Call<DefaultResponse>

    //refresh auth token
    @FormUrlEncoded
    @POST("admin/auth_admin/refreshToken")
    fun refreshAuthToken(
        @Field("iadmin") iadmin: String
    ): Call<LoginResponse>

    //get Foto User
    @FormUrlEncoded
    @POST("admin/auth_admin/getUserFoto")
    fun getUserFoto(
        @Field("idadmin") idadmin: String
    ): Call<LoginResponse>
}