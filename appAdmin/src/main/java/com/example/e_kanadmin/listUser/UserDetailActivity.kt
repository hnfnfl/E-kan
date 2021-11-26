package com.example.e_kanadmin.listUser

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.e_kanadmin.MainActivity
import com.example.e_kanadmin.R
import com.example.e_kanadmin.databinding.ActivityUserDetailBinding
import com.example.e_kanadmin.retrofit.DataService
import com.example.e_kanadmin.retrofit.RetrofitClient
import com.example.e_kanadmin.retrofit.response.DefaultResponse
import com.example.e_kanadmin.utils.Constants
import com.example.e_kanadmin.utils.MySharedPreferences
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailActivity : AppCompatActivity() {

    private lateinit var userDetailBinding: ActivityUserDetailBinding
    private lateinit var myPreferences: MySharedPreferences

    companion object {
        const val iduser = "iduser"
        const val nama = "nama"
        const val alamat = "alamat"
        const val email = "email"
        const val nohp = "nohp"
        const val status = "status"
        const val foto_path = "foto_path"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userDetailBinding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(userDetailBinding.root)

        myPreferences = MySharedPreferences(this@UserDetailActivity)

        val iduser = intent.getStringExtra(iduser).toString()
        val nama = intent.getStringExtra(nama).toString()
        val alamat = intent.getStringExtra(alamat).toString()
        val email = intent.getStringExtra(email).toString()
        val nohp = intent.getStringExtra(nohp).toString()
        val status = intent.getStringExtra(status).toString()
        val fotoPath = intent.getStringExtra(foto_path).toString()
        val tokenAuth = myPreferences.getValue(Constants.TokenAuth).toString()

        userDetailBinding.tvDetailName.text = nama
        userDetailBinding.tvUserAddress.text = alamat
        userDetailBinding.tvUserEmail.text = email
        userDetailBinding.tvUserPhone.text = nohp
        Glide.with(this@UserDetailActivity)
            .load(fotoPath)
            .transform(CenterCrop())
            .apply(RequestOptions().override(550, 0))
            .placeholder(R.drawable.avatar_default)
            .error(R.drawable.avatar_default)
            .into(userDetailBinding.imgUser)

        if (status == "show") {
            userDetailBinding.tvStatus.text = getString(R.string.user_not_blocked)
            userDetailBinding.btnBlockUser.visibility = View.VISIBLE
        } else {
            userDetailBinding.tvStatus.text = getString(R.string.user_blocked)
            userDetailBinding.btnShowUser.visibility = View.VISIBLE
        }

        userDetailBinding.btnBack.setOnClickListener {
            onBackPressed()
        }

        userDetailBinding.btnBlockUser.setOnClickListener {
            userDetailBinding.btnBlockUser.startAnimation()
            editStatusUser(iduser, "blocked", tokenAuth)
        }

        userDetailBinding.btnShowUser.setOnClickListener {
            userDetailBinding.btnShowUser.startAnimation()
            editStatusUser(iduser, "show", tokenAuth)
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@UserDetailActivity, ListUserActivity::class.java))
        finish()
    }

    private fun editStatusUser(iduser: String, status: String, tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.editStatusUser(iduser, status, "Bearer $tokenAuth").enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        startActivity(Intent(this@UserDetailActivity, ListUserActivity::class.java))
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toasty.error(this@UserDetailActivity, R.string.try_again, Toasty.LENGTH_LONG).show()
            }
        })
    }
}