package com.example.e_kan.auth.register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.e_kan.R.*
import com.example.e_kan.auth.login.LoginActivity
import com.example.e_kan.databinding.ActivityRegisterBinding
import com.example.e_kan.retrofit.AuthService
import com.example.e_kan.retrofit.RetrofitClient
import com.example.e_kan.retrofit.response.DefaultResponse
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerBinding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerBinding.root)

        registerBinding.login.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }

        registerBinding.btnRegister.setOnClickListener {
            if (isDataFilled()) {
                val email = registerBinding.tvValueEmailRegister.text.toString()
                precheck(email)
                registerBinding.btnRegister.startAnimation()
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

    private fun precheck(email: String) {
        val service = RetrofitClient().apiRequest().create(AuthService::class.java)
        service.precheck(email).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    when (response.body()!!.status) {
                        "success" -> {
                            requestOtp(email)
                        }
                        "failed" -> {
                            registerBinding.btnRegister.endAnimation()
                            Toasty.warning(this@RegisterActivity, response.body()!!.message, Toasty.LENGTH_LONG).show()
                        }
                    }
                } else {
                    registerBinding.btnRegister.endAnimation()
                    Toasty.error(this@RegisterActivity, string.try_again, Toasty.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                registerBinding.btnRegister.endAnimation()
                Toasty.error(this@RegisterActivity, string.try_again, Toasty.LENGTH_LONG).show()
            }
        })
    }

    private fun requestOtp(email: String) {
        val service = RetrofitClient().apiRequest().create(AuthService::class.java)
        service.requestOtp(email).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        val nama = registerBinding.tvValueNameRegister.text.toString()
                        val namaToko = registerBinding.tvValueShopRegister.text.toString()
                        val alamat = registerBinding.tvValueAddressRegister.text.toString()
                        val nohp = registerBinding.tvValueNohpRegister.text.toString()
                        val password = registerBinding.tvValuePasswordRegister.text.toString()
                        val emailConfirm = Intent(this@RegisterActivity, OTPActivity::class.java)
                            .apply {
                                putExtra(OTPActivity.nama, nama)
                                putExtra(OTPActivity.namaToko, namaToko)
                                putExtra(OTPActivity.alamat, alamat)
                                putExtra(OTPActivity.email, email)
                                putExtra(OTPActivity.nohp, nohp)
                                putExtra(OTPActivity.password, password)
                            }
                        startActivity(emailConfirm)
                    }
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toasty.error(this@RegisterActivity, string.try_again, Toasty.LENGTH_LONG).show()
            }

        })
    }
}