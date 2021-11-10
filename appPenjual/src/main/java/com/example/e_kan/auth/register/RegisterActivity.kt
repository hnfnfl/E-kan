package com.example.e_kan.auth.register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.e_kan.auth.login.LoginActivity
import com.example.e_kan.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerBinding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerBinding.root)

        registerBinding.login.setOnClickListener{
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }

        registerBinding.btnRegister.setOnClickListener {
            if (isDataFilled()) {
                startActivity(Intent(this@RegisterActivity, OTPActivity::class.java))
            }
        }

    }

    private fun isDataFilled(): Boolean {
        fun String.isValidEmail() = isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
        if (registerBinding.tvValueNameRegister.text.toString() == "") {
            registerBinding.tvValueNameRegister.error = "Nama tidak boleh kosong"
            registerBinding.tvValueNameRegister.requestFocus()
            return false
        } else if (registerBinding.tvValueAddressRegister.text.toString() == "") {
            registerBinding.tvValueAddressRegister.error = "Alamat tidak boleh kosong"
            registerBinding.tvValueAddressRegister.requestFocus()
            return false
        } else if (registerBinding.tvValueEmailRegister.text.toString() == "") {
            registerBinding.tvValueEmailRegister.error = "Email tidak boleh kosong"
            registerBinding.tvValueEmailRegister.requestFocus()
            return false
        } else if (!registerBinding.tvValueEmailRegister.text.toString().isValidEmail()) {
            registerBinding.tvValueEmailRegister.error = "Format email salah"
            registerBinding.tvValueEmailRegister.requestFocus()
            return false
        } else if (registerBinding.tvValueNohpRegister.text.toString() == "") {
            registerBinding.tvValueNohpRegister.error = "Nomor HP tidak boleh kosong"
            registerBinding.tvValueNohpRegister.requestFocus()
            return false
        } else if (registerBinding.tvValuePasswordRegister.text.toString() == "") {
            registerBinding.tvValuePasswordRegister.error = "Kata sandi tidak boleh kosong"
            registerBinding.tvValuePasswordRegister.requestFocus()
            return false
        }
        return true
    }
}