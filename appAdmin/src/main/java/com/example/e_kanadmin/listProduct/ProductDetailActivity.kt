package com.example.e_kanadmin.listProduct

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.e_kanadmin.R
import com.example.e_kanadmin.databinding.ActivityProductDetailBinding
import com.example.e_kanadmin.retrofit.DataService
import com.example.e_kanadmin.retrofit.RetrofitClient
import com.example.e_kanadmin.retrofit.response.DefaultResponse
import com.example.e_kanadmin.utils.Constants
import com.example.e_kanadmin.utils.MySharedPreferences
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var productDetailBinding: ActivityProductDetailBinding
    private lateinit var myPreferences: MySharedPreferences

    companion object {
        const val idproduk = "idpenjual"
        const val nama_toko = "nama_toko"
        const val nama_produk = "nama_produk"
        const val keterangan = "keterangan"
        const val harga = "harga"
        const val statusProduk = "statusProduk"
        const val foto_path = "foto_path"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productDetailBinding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(productDetailBinding.root)

        myPreferences = MySharedPreferences(this@ProductDetailActivity)
        val idproduk = intent.getStringExtra(idproduk).toString()
        val namaToko = intent.getStringExtra(nama_toko).toString()
        val namaProduk = intent.getStringExtra(nama_produk).toString()
        val keterangan = intent.getStringExtra(keterangan).toString()
        val harga = intent.getStringExtra(harga).toString()
        val status = intent.getStringExtra(statusProduk).toString()
        val fotoPath = intent.getStringExtra(foto_path).toString()
        val tokenAuth = getString(R.string.token_auth, myPreferences.getValue(Constants.TokenAuth).toString())

        productDetailBinding.tvDetailName.text = namaProduk
        productDetailBinding.tvSellerShop.text = namaToko
        val numbering = DecimalFormat("#,###")
        val price = numbering.format(harga.toInt())
        productDetailBinding.tvProductPrice.text = getString(R.string.product_price, price)
        productDetailBinding.tvProductDesc.text = keterangan
        Glide.with(this@ProductDetailActivity)
            .load(fotoPath)
            .transform(CenterCrop())
            .apply(RequestOptions().override(550, 0))
            .placeholder(R.drawable.avatar_default)
            .error(R.drawable.avatar_default)
            .into(productDetailBinding.imgProduct)

        if (status == "show") {
            productDetailBinding.tvStatus.text = getString(R.string.product_show)
            productDetailBinding.btnHideProduct.visibility = View.VISIBLE
        } else {
            productDetailBinding.tvStatus.text = getString(R.string.product_hidden)
            productDetailBinding.btnShowProduct.visibility = View.VISIBLE
        }

        productDetailBinding.btnBack.setOnClickListener {
            onBackPressed()
        }

        productDetailBinding.btnShowProduct.setOnClickListener {
            productDetailBinding.btnShowProduct.startAnimation()
            editStatusProduct(idproduk, "show", tokenAuth)
        }

        productDetailBinding.btnHideProduct.setOnClickListener {
            productDetailBinding.btnHideProduct.startAnimation()
            editStatusProduct(idproduk, "hidden", tokenAuth)
        }

    }

    override fun onBackPressed() {
        startActivity(Intent(this@ProductDetailActivity, ListProductActivity::class.java))
        finish()
    }

    private fun editStatusProduct(idproduk: String, status: String, tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.editStatusProduct(idproduk, status, tokenAuth).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == getString(R.string.success)) {
                        startActivity(Intent(this@ProductDetailActivity, ListProductActivity::class.java))
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toasty.error(this@ProductDetailActivity, R.string.try_again, Toasty.LENGTH_LONG).show()
            }
        })
    }
}