package com.example.e_kanadmin.transaction

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.e_kanadmin.MainActivity
import com.example.e_kanadmin.R
import com.example.e_kanadmin.databinding.ActivityTransactionDetailBinding
import com.example.e_kanadmin.retrofit.DataService
import com.example.e_kanadmin.retrofit.RetrofitClient
import com.example.e_kanadmin.retrofit.response.DefaultResponse
import com.example.e_kanadmin.utils.Constants
import com.example.e_kanadmin.utils.MySharedPreferences
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TransactionDetailActivity : AppCompatActivity() {

    private lateinit var transactionDetailBinding: ActivityTransactionDetailBinding
    private lateinit var myPreferences: MySharedPreferences
    private lateinit var transactionOrderItemAdapter: TransactionOrderItemAdapter

    companion object {
        const val idpesanan = "idpesanan"
        const val nama = "nama"
        const val namaToko = "namaToko"
        const val kodePesanan = "kodePesanan"
        const val tglPesan = "tglPesan"
        const val tglTransfer = "tglTransfer"
        const val catatan = "catatan"
        const val alamat = "alamat"
        const val fotoPath = "fotoPath"
        const val status = "status"
        const val transaksi = "transaksi"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transactionDetailBinding = ActivityTransactionDetailBinding.inflate(layoutInflater)
        setContentView(transactionDetailBinding.root)

        myPreferences = MySharedPreferences(this@TransactionDetailActivity)
        transactionOrderItemAdapter = TransactionOrderItemAdapter()
        val idpesanan = intent.getStringExtra(idpesanan).toString()
        val nama = intent.getStringExtra(nama).toString()
        val namaToko = intent.getStringExtra(namaToko).toString()
        val kodePesanan = intent.getStringExtra(kodePesanan).toString()
        val tglPesan = intent.getStringExtra(tglPesan).toString()
        val tglTransfer = intent.getStringExtra(tglTransfer).toString()
        val catatan = intent.getStringExtra(catatan).toString()
        val alamat = intent.getStringExtra(alamat).toString()
        val fotoPath = intent.getStringExtra(fotoPath).toString()
        val status = intent.getStringExtra(status).toString()
        val transaksi = intent.getStringExtra(transaksi).toString()
        val tokenAuth = myPreferences.getValue(Constants.TokenAuth).toString()

        transactionDetailBinding.btnBack.setOnClickListener {
            onBackPressed()
        }

        transactionDetailBinding.btnApprove.setOnClickListener {
            transactionDetailBinding.btnApprove.startAnimation()
            val editTransaksi = getString(R.string.approved)
            editStatus(idpesanan, editTransaksi, tokenAuth)
        }

        transactionDetailBinding.btnCancel.setOnClickListener {
            transactionDetailBinding.btnCancel.startAnimation()
            val editTransaksi = getString(R.string.canceled)
            editStatus(idpesanan, editTransaksi, tokenAuth)
        }

        transactionDetailBinding.btnReject.setOnClickListener {
            transactionDetailBinding.btnReject.startAnimation()
            val editTransaksi = getString(R.string.rejected)
            editStatus(idpesanan, editTransaksi, tokenAuth)
        }

        transactionDetailBinding.tvKodePesanan.text = kodePesanan
        transactionDetailBinding.tvDate.text = tglPesan
        transactionDetailBinding.tvUserName.text = nama
        transactionDetailBinding.tvShopName.text = namaToko
        transactionDetailBinding.tvNote.text = catatan
        transactionDetailBinding.tvDetailAddress.text = alamat
        transactionDetailBinding.tvTransferDate.text = tglTransfer
        if (transaksi == getString(R.string.pending)) {
            transactionDetailBinding.btnApprove.visibility = View.VISIBLE
            transactionDetailBinding.btnReject.visibility = View.VISIBLE
            transactionDetailBinding.btnCancel.visibility = View.VISIBLE
        }
        Glide.with(this@TransactionDetailActivity)
            .load(fotoPath)
            .placeholder(R.drawable.ic_profile)
            .error(R.drawable.ic_profile)
            .into(transactionDetailBinding.imgBuktiTransfer)

        val list = intent.getParcelableArrayListExtra<OrderItemEntity>("pesanan") as ArrayList<OrderItemEntity>
        transactionOrderItemAdapter.setOrderItem(list)
        transactionOrderItemAdapter.notifyDataSetChanged()

        with(transactionDetailBinding.rvOrderItem) {
            layoutManager = LinearLayoutManager(this@TransactionDetailActivity)
            itemAnimator = DefaultItemAnimator()
            setHasFixedSize(true)
            adapter = transactionOrderItemAdapter
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@TransactionDetailActivity, MainActivity::class.java))
        finish()
    }

    private fun editStatus(idpenjual: String, transaksi: String, tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.editStatus(idpenjual, transaksi, "Bearer $tokenAuth").enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        startActivity(Intent(this@TransactionDetailActivity, MainActivity::class.java))
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toasty.error(this@TransactionDetailActivity, R.string.try_again, Toasty.LENGTH_LONG).show()
            }
        })
    }
}