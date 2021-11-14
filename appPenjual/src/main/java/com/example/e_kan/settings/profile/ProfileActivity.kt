package com.example.e_kan.settings.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.e_kan.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var profileBinding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileBinding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(profileBinding.root)

        profileBinding.btnBack.setOnClickListener {
            onBackPressed()
            finish()
        }

        profileBinding.btnSave.setOnClickListener {
            profileBinding.btnSave.startAnimation()
            onBackPressed()
            finish()
        }
    }
}