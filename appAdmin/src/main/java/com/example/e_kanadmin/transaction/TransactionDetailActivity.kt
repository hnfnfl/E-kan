package com.example.e_kanadmin.transaction

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.e_kanadmin.databinding.ActivityTransactionDetailBinding
import com.example.e_kanadmin.utils.MySharedPreferences

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
    }
}