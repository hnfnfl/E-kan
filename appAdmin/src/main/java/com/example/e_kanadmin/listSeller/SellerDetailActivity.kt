package com.example.e_kanadmin.listSeller

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.e_kanadmin.R
import com.example.e_kanadmin.databinding.ActivitySellerDetailBinding
import com.example.e_kanadmin.retrofit.DataService
import com.example.e_kanadmin.retrofit.RetrofitClient
import com.example.e_kanadmin.retrofit.response.DefaultResponse
import com.example.e_kanadmin.utils.Constants
import com.example.e_kanadmin.utils.MySharedPreferences
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SellerDetailActivity : AppCompatActivity() {

    private lateinit var sellerDetailBinding: ActivitySellerDetailBinding
    private lateinit var myPreferences: MySharedPreferences

    companion object {
        const val idpenjual = "idpenjual"
        const val nama_penjual = "nama_penjual"
        const val nama_toko = "nama_toko"
        const val alamat_penjual = "alamat_penjual"
        const val email_penjual = "email_penjual"
        const val nohp_penjual = "email_penjual"
        const val statusPenjual = "statusPenjual"
        const val rating = "status"
        const val foto_path = "foto_path"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sellerDetailBinding = ActivitySellerDetailBinding.inflate(layoutInflater)
        setContentView(sellerDetailBinding.root)

        myPreferences = MySharedPreferences(this@SellerDetailActivity)

        val idpenjual = intent.getStringExtra(idpenjual).toString()
        val namaPenjual = intent.getStringExtra(nama_penjual).toString()
        val namaToko = intent.getStringExtra(nama_toko).toString()
        val emailPenjual = intent.getStringExtra(email_penjual).toString()
        val alamatPenjual = intent.getStringExtra(alamat_penjual).toString()
        val nohpPenjual = intent.getStringExtra(nohp_penjual).toString()
        val status = intent.getStringExtra(statusPenjual).toString()
        val fotoPath = intent.getStringExtra(foto_path).toString()
        val tokenAuth = getString(R.string.token_auth, myPreferences.getValue(Constants.TokenAuth).toString())

        sellerDetailBinding.tvDetailName.text = namaPenjual
        sellerDetailBinding.tvSellerShop.text = namaToko
        sellerDetailBinding.tvSellerEmail.text = emailPenjual
        sellerDetailBinding.tvSellerPhone.text = nohpPenjual
        sellerDetailBinding.tvSellerAddress.text = alamatPenjual
        sellerDetailBinding.tvStatus.text = status
        Glide.with(this@SellerDetailActivity)
            .load(fotoPath)
            .transform(CenterCrop())
            .apply(RequestOptions().override(550, 0))
            .placeholder(R.drawable.avatar_default)
            .error(R.drawable.avatar_default)
            .into(sellerDetailBinding.imgSeller)

        if (status == "show") {
            sellerDetailBinding.tvStatus.text = getString(R.string.user_not_blocked)
            sellerDetailBinding.btnBlockSeller.visibility = View.VISIBLE
        } else {
            sellerDetailBinding.tvStatus.text = getString(R.string.user_blocked)
            sellerDetailBinding.btnShowSeller.visibility = View.VISIBLE
        }

        sellerDetailBinding.btnBack.setOnClickListener {
            onBackPressed()
        }

        sellerDetailBinding.btnBlockSeller.setOnClickListener {
            sellerDetailBinding.btnBlockSeller.startAnimation()
            editStatusSeller(idpenjual, "blocked", tokenAuth)
        }

        sellerDetailBinding.btnShowSeller.setOnClickListener {
            sellerDetailBinding.btnShowSeller.startAnimation()
            editStatusSeller(idpenjual, "show", tokenAuth)
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@SellerDetailActivity, ListSellerActivity::class.java))
        finish()
    }

    private fun editStatusSeller(idpenjual: String, status: String, tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.editStatusSeller(idpenjual, status, tokenAuth).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        startActivity(Intent(this@SellerDetailActivity, ListSellerActivity::class.java))
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toasty.error(this@SellerDetailActivity, R.string.try_again, Toasty.LENGTH_LONG).show()
            }
        })
    }
}