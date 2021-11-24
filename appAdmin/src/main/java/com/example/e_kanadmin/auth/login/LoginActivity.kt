package com.example.e_kanadmin.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.example.e_kanadmin.utils.MySharedPreferences
import com.example.e_kanadmin.MainActivity
import com.example.e_kanadmin.R.*
import com.example.e_kanadmin.auth.register.RegisterActivity
import com.example.e_kanadmin.databinding.ActivityLoginBinding
import com.example.e_kanadmin.retrofit.AuthService
import com.example.e_kanadmin.retrofit.RetrofitClient
import com.example.e_kanadmin.retrofit.response.LoginResponse
import com.example.e_kanadmin.utils.Constants
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

//        Ketika user sudah login tidak perlu ke halaman login lagi
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
        fun String.isValidEmail() = isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
        if (loginBinding.tvValueEmailLogin.text.toString() == "") {
            loginBinding.tvValueEmailLogin.error = getString(string.email_cant_empty)
            loginBinding.tvValueEmailLogin.requestFocus()
            return false
        } else if (!loginBinding.tvValueEmailLogin.text.toString().isValidEmail()) {
            loginBinding.tvValueEmailLogin.error = getString(string.email_format_error)
            loginBinding.tvValueEmailLogin.requestFocus()
            return false
        } else if (loginBinding.tvValuePasswordLogin.text.toString() == "") {
            loginBinding.tvValuePasswordLogin.error = getString(string.password_cant_empty)
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
                            myPreferences.setValue(Constants.USER_ID, response.body()!!.data[0].idadmin)
                            myPreferences.setValue(Constants.USER_NAMA, response.body()!!.data[0].nama_admin)
                            myPreferences.setValue(Constants.USER_EMAIL, response.body()!!.data[0].email_admin)
                            myPreferences.setValue(Constants.USER_NOHP, response.body()!!.data[0].nohp_admin)
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
                Toasty.error(this@LoginActivity, t.message.toString(), Toasty.LENGTH_LONG).show()
            }

        })
    }
}