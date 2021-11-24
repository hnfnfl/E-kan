package com.example.e_kanadmin.auth.register

import `in`.aabhasjindal.otptextview.OTPListener
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_kanadmin.R.*
import com.example.e_kanadmin.auth.login.LoginActivity
import com.example.e_kanadmin.databinding.ActivityOtpactivityBinding
import com.example.e_kanadmin.retrofit.AuthService
import com.example.e_kanadmin.retrofit.RetrofitClient
import com.example.e_kanadmin.retrofit.response.DefaultResponse
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OTPActivity : AppCompatActivity() {

    private lateinit var otpBinding: ActivityOtpactivityBinding

    companion object {
        const val nama = "nama"
        const val email = "email"
        const val nohp = "nohp"
        const val password = "password"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        otpBinding = ActivityOtpactivityBinding.inflate(layoutInflater)
        setContentView(otpBinding.root)

        otpBinding.resendCode.setOnClickListener {
            val email = intent.getStringExtra(email).toString()
            requestOtp(email)
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
                            val nohp = intent.getStringExtra(nohp).toString()
                            val password = intent.getStringExtra(password).toString()
                            registrasi(nama, email, nohp, password)
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

    private fun registrasi(nama: String, email: String, nohp: String, password: String) {
        val service = RetrofitClient().apiRequest().create(AuthService::class.java)
        service.register(nama, email, nohp, password).enqueue(object : Callback<DefaultResponse> {
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

    private fun requestOtp(email: String) {
        val service = RetrofitClient().apiRequest().create(AuthService::class.java)
        service.requestOtp(email).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        Toast.makeText(this@OTPActivity, "Kode sudah dikirim ulang ke email anda!", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toasty.error(this@OTPActivity, string.try_again, Toasty.LENGTH_LONG).show()
            }

        })
    }
}