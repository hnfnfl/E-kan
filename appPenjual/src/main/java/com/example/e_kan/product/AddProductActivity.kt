package com.example.e_kan.product

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.e_kan.databinding.ActivityAddProductBinding

class AddProductActivity : AppCompatActivity() {

    private lateinit var addProductBinding: ActivityAddProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addProductBinding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(addProductBinding.root)

        addProductBinding.btnBack.setOnClickListener {
            onBackPressed()
            finish()
        }

        addProductBinding.btnAddProduct.setOnClickListener {
            addProductBinding.btnAddProduct.startAnimation()
            startActivity(Intent(this@AddProductActivity, ListProductActivity::class.java))
            finish()
        }
    }
}