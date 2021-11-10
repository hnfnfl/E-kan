package com.example.e_kan.auth.register

import `in`.aabhasjindal.otptextview.OTPListener
import `in`.aabhasjindal.otptextview.OtpTextView
import android.R
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_kan.auth.login.LoginActivity
import com.example.e_kan.databinding.ActivityOtpBinding


class OTPActivity : AppCompatActivity() {

    private lateinit var otpBinding: ActivityOtpBinding

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
                if (otp == "1234") {
                    otpTextView.showSuccess()
                    startActivity(Intent(this@OTPActivity, LoginActivity::class.java))
                    Toast.makeText(this@OTPActivity, "OTP benar", Toast.LENGTH_SHORT).show()
                } else {
                    otpTextView.showError()
                    Toast.makeText(this@OTPActivity, "OTP salah", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}