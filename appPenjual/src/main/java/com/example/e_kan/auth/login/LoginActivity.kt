package com.example.e_kan.auth.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.e_kan.MainActivity
import com.example.e_kan.R.*
import com.example.e_kan.auth.register.RegisterActivity
import com.example.e_kan.databinding.ActivityLoginBinding
import com.example.e_kan.retrofit.AuthService
import com.example.e_kan.retrofit.RetrofitClient
import com.example.e_kan.retrofit.response.LoginResponse
import com.example.e_kan.utils.Constants
import com.example.e_kan.utils.MySharedPreferences
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var loginBinding: ActivityLoginBinding
    private lateinit var myPreferences: MySharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)
        myPreferences = MySharedPreferences(this@LoginActivity)

        //Ketika user sudah login tidak perlu ke halaman login lagi
        if (myPreferences.getValue(Constants.USER).equals(Constants.LOGIN)) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
            return
        }

        loginBinding.register.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            finish()
        }

        loginBinding.btnLogin.setOnClickListener {
            val email = loginBinding.tvValueEmailLogin.text.toString()
            val pass = loginBinding.tvValuePasswordLogin.text.toString()
            if (isDataFilled()) {
                loginProcess(email, pass)
                loginBinding.btnLogin.startAnimation()
            }
        }
    }

    private fun isDataFilled(): Boolean {
        fun String.isValidEmail() = isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
        if (loginBinding.tvValueEmailLogin.text.toString() == "") {
            loginBinding.tvValueEmailLogin.error = "Email tidak boleh kosong"
            loginBinding.tvValueEmailLogin.requestFocus()
            return false
        } else if (!loginBinding.tvValueEmailLogin.text.toString().isValidEmail()) {
            loginBinding.tvValueEmailLogin.error = "Format email salah"
            loginBinding.tvValueEmailLogin.requestFocus()
            return false
        } else if (loginBinding.tvValuePasswordLogin.text.toString() == "") {
            loginBinding.tvValuePasswordLogin.error = "Kata sandi tidak boleh kosong"
            loginBinding.tvValuePasswordLogin.requestFocus()
            return false
        }
        return true
    }

    private fun loginProcess(email: String, password: String) {
        val service = RetrofitClient().apiRequest().create(AuthService::class.java)
        service.login(email, password).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    when (response.body()!!.status) {
                        "success" -> {
                            myPreferences.setValue(Constants.USER, Constants.LOGIN)
                            myPreferences.setValue(Constants.USER_ID, response.body()!!.data[0].idpenjual)
                            myPreferences.setValue(Constants.USER_NAMA, response.body()!!.data[0].nama_penjual)
                            myPreferences.setValue(Constants.USER_NAMA_TOKO, response.body()!!.data[0].nama_toko)
                            myPreferences.setValue(Constants.USER_ALAMAT, response.body()!!.data[0].alamat_penjual)
                            myPreferences.setValue(Constants.USER_EMAIL, response.body()!!.data[0].email_penjual)
                            myPreferences.setValue(Constants.USER_NOHP, response.body()!!.data[0].nohp_penjual)
                            myPreferences.setValue(Constants.DEVICE_TOKEN, response.body()!!.data[0].device_token)
                            myPreferences.setValue(Constants.FOTO_NAME, response.body()!!.data[0].foto_name)
                            myPreferences.setValue(Constants.FOTO_PATH, response.body()!!.data[0].foto_path)

                            myPreferences.setValue(Constants.TokenAuth, response.body()!!.tokenAuth)
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        }
                        "failed" -> {
                            loginBinding.btnLogin.endAnimation()
                            Toasty.error(this@LoginActivity, string.email_pass_not_match, Toasty.LENGTH_LONG).show()
                        }
                        "not_exist" -> {
                            loginBinding.btnLogin.endAnimation()
                            Toasty.error(this@LoginActivity, string.email_not_registered, Toasty.LENGTH_LONG).show()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                loginBinding.btnLogin.endAnimation()
                Toasty.error(this@LoginActivity, string.try_again, Toasty.LENGTH_LONG).show()
            }

        })
    }
}