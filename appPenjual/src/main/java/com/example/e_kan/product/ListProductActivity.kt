package com.example.e_kan.product

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.e_kan.databinding.ActivityListProductBinding

class ListProductActivity : AppCompatActivity() {

    private lateinit var listProductBinding: ActivityListProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listProductBinding = ActivityListProductBinding.inflate(layoutInflater)
        setContentView(listProductBinding.root)

        listProductBinding.btnBack.setOnClickListener {
            onBackPressed()
            finish()
        }

        listProductBinding.btnAddProduct.setOnClickListener {
            startActivity(Intent(this@ListProductActivity, AddProductActivity::class.java))
            finish()
        }
    }
}