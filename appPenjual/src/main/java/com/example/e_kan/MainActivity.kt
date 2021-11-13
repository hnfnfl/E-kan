package com.example.e_kan

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.e_kan.databinding.ActivityMainBinding
import com.example.e_kan.product.ListProductActivity

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.llProduct.setOnClickListener {
            startActivity(Intent(this@MainActivity, ListProductActivity::class.java))
        }
    }
}