package com.example.e_kan.auth.register

import `in`.aabhasjindal.otptextview.OTPListener
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_kan.R.*
import com.example.e_kan.auth.login.LoginActivity
import com.example.e_kan.databinding.ActivityOtpBinding
import com.example.e_kan.retrofit.AuthService
import com.example.e_kan.retrofit.RetrofitClient
import com.example.e_kan.retrofit.response.DefaultResponse
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OTPActivity : AppCompatActivity() {

    private lateinit var otpBinding: ActivityOtpBinding

    companion object {
        const val nama = "nama"
        const val namaToko = "namaToko"
        const val alamat = "alamat"
        const val email = "email"
        const val nohp = "nohp"
        const val password = "password"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        otpBinding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(otpBinding.root)

        otpBinding.resendCode.setOnClickListener {
            Toast.makeText(this@OTPActivity, "Kode sudah dikirim ulang ke email anda!", Toast.LENGTH_SHORT).show()
        }

        val otpTextView = otpBinding.otpView
        otpTextView.otpListener = object : OTPListener {
            override fun onInteractionListener() {
                // fired when user types something in the Otpbox
            }

            override fun onOTPComplete(otp: String) {
                // fired when user has entered the OTP fully.
                val email = intent.getStringExtra(email).toString()
                verifyOtp(email, otp)
            }
        }
    }

    private fun verifyOtp(email: String, otp: String) {
        val service = RetrofitClient().apiRequest().create(AuthService::class.java)
        service.verifyOtp(email, otp).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    when (response.body()!!.status) {
                        "success" -> {
                            otpBinding.otpView.showSuccess()
                            val nama = intent.getStringExtra(nama).toString()
                            val namaToko = intent.getStringExtra(namaToko).toString()
                            val alamat = intent.getStringExtra(alamat).toString()
                            val nohp = intent.getStringExtra(nohp).toString()
                            val password = intent.getStringExtra(password).toString()
                            registrasi(nama, namaToko, alamat, email, nohp, password)
                        }
                        "not_match" -> {
                            otpBinding.otpView.showError()
                            otpBinding.otpView.resetState()
                            Toasty.error(this@OTPActivity, string.otp_not_match, Toasty.LENGTH_LONG).show()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toasty.error(this@OTPActivity, string.try_again, Toasty.LENGTH_LONG).show()
            }
        })
    }

    private fun registrasi(nama: String, nama_toko: String, alamat: String, email: String, nohp: String, password: String) {
        val service = RetrofitClient().apiRequest().create(AuthService::class.java)
        service.register(nama, nama_toko, alamat, email, nohp, password).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    when (response.body()!!.status) {
                        "success" -> {
                            startActivity(Intent(this@OTPActivity, LoginActivity::class.java))
                        }
                        "failed" -> {
                            Toasty.warning(this@OTPActivity, response.body()!!.message, Toasty.LENGTH_LONG).show()
                        }
                    }
                } else {
                    Toasty.error(this@OTPActivity, string.try_again, Toasty.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toasty.error(this@OTPActivity, string.try_again, Toasty.LENGTH_LONG).show()
            }
        })
    }
}