package com.example.e_kanadmin.settings

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.e_kanadmin.MainActivity
import com.example.e_kanadmin.R
import com.example.e_kanadmin.auth.login.LoginActivity
import com.example.e_kanadmin.databinding.ActivitySettingBinding
import com.example.e_kanadmin.settings.profile.ProfileActivity
import com.example.e_kanadmin.utils.Constants
import com.example.e_kanadmin.utils.MySharedPreferences
import dev.shreyaspatil.MaterialDialog.MaterialDialog

class SettingActivity : AppCompatActivity() {

    private lateinit var settingBinding: ActivitySettingBinding
    private lateinit var myPreferences: MySharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingBinding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(settingBinding.root)

        myPreferences = MySharedPreferences(this@SettingActivity)

        settingBinding.btnEditProfile.setOnClickListener {
            startActivity(Intent(this@SettingActivity, ProfileActivity::class.java))
        }

        settingBinding.btnBack.setOnClickListener {
            onBackPressed()
        }

        settingBinding.btnLogout.setOnClickListener {
            val mDialog = MaterialDialog.Builder(this@SettingActivity)
                .setTitle("Logout")
                .setMessage(getString(R.string.confirm_logout))
                .setCancelable(true)
                .setPositiveButton(
                    getString(R.string.no), R.drawable.ic_close
                ) { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
                .setNegativeButton(
                    getString(R.string.yes), R.drawable.ic_logout
                ) { dialogInterface, _ ->
                    myPreferences.setValue(Constants.USER, "")
                    myPreferences.setValue(Constants.USER_ID, "")
                    myPreferences.setValue(Constants.USER_NAMA, "")
                    myPreferences.setValue(Constants.USER_EMAIL, "")
                    myPreferences.setValue(Constants.USER_NOHP, "")
                    myPreferences.setValue(Constants.DEVICE_TOKEN, "")
                    myPreferences.setValue(Constants.FOTO_NAME, "")
                    myPreferences.setValue(Constants.FOTO_PATH, "")

                    startActivity(Intent(this@SettingActivity, LoginActivity::class.java))
                    finish()
                    dialogInterface.dismiss()
                }
                .build()
            // Show Dialog
            mDialog.show()
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@SettingActivity, MainActivity::class.java))
        finish()
    }
}