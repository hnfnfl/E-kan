package com.example.e_kan.orderList

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.e_kan.databinding.ActivityOrderListBinding

class OrderListActivity : AppCompatActivity() {

    private lateinit var orderListBinding: ActivityOrderListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderListBinding = ActivityOrderListBinding.inflate(layoutInflater)
        setContentView(orderListBinding.root)

        orderListBinding.btnBack.setOnClickListener {
            onBackPressed()
            finish()
        }
    }
}