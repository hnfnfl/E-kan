package com.example.e_kan

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.e_kan.databinding.ActivityMainBinding
import com.example.e_kan.notification.NotificationActivity
import com.example.e_kan.orderList.OrderListActivity
import com.example.e_kan.product.ListProductActivity
import com.example.e_kan.settings.SettingActivity
import com.example.e_kan.settings.profile.ProfileActivity

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.llProduct.setOnClickListener {
            startActivity(Intent(this@MainActivity, ListProductActivity::class.java))
        }

        mainBinding.llOrderHistory.setOnClickListener {
            startActivity(Intent(this@MainActivity, OrderListActivity::class.java))
        }

        mainBinding.llSetting.setOnClickListener {
            startActivity(Intent(this@MainActivity, SettingActivity::class.java))
        }

        mainBinding.btnProfile.setOnClickListener {
            startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
        }

        mainBinding.btnNotif.setOnClickListener {
            startActivity(Intent(this@MainActivity, NotificationActivity::class.java))
        }
    }
}