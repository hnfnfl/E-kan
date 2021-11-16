package com.example.e_kan.product

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.e_kan.databinding.ActivityEditProductBinding

class EditProductActivity : AppCompatActivity() {

    private lateinit var editProductBinding: ActivityEditProductBinding

    companion object {
        const val nama = "nama"
        const val keterangan = "keterangan"
        const val harga = "harga"
        const val berat = "berat"
        const val stok = "stok"
        const val foto_path = "foto_path"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editProductBinding = ActivityEditProductBinding.inflate(layoutInflater)
        setContentView(editProductBinding.root)

        val nama = intent.getStringExtra(nama).toString()
        val keterangan = intent.getStringExtra(keterangan).toString()
        val harga = intent.getStringExtra(harga).toString()
        val berat = intent.getStringExtra(berat).toString()
        val stok = intent.getStringExtra(stok).toString()



    }
}