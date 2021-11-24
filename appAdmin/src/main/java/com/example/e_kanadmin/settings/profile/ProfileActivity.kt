package com.example.e_kanadmin.settings.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.e_kanadmin.MainActivity
import com.example.e_kanadmin.R
import com.example.e_kanadmin.databinding.ActivityProfileBinding
import com.example.e_kanadmin.retrofit.AuthService
import com.example.e_kanadmin.retrofit.DataService
import com.example.e_kanadmin.retrofit.RetrofitClient
import com.example.e_kanadmin.retrofit.response.DefaultResponse
import com.example.e_kanadmin.retrofit.response.LoginResponse
import com.example.e_kanadmin.settings.SettingActivity
import com.example.e_kanadmin.utils.Constants
import com.example.e_kanadmin.utils.FileUtils
import com.example.e_kanadmin.utils.MySharedPreferences
import com.github.dhaval2404.imagepicker.ImagePicker
import es.dmoral.toasty.Toasty
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    private lateinit var profileBinding: ActivityProfileBinding
    private lateinit var myPreferences: MySharedPreferences
    var photoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileBinding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(profileBinding.root)

        myPreferences = MySharedPreferences(this@ProfileActivity)

        val nama = myPreferences.getValue(Constants.USER_NAMA).toString()
        val email = myPreferences.getValue(Constants.USER_EMAIL).toString()
        val nohp = myPreferences.getValue(Constants.USER_NOHP).toString()
        val fotoPath = myPreferences.getValue(Constants.FOTO_PATH).toString()

        profileBinding.tvValueNameEdit.setText(nama)
        profileBinding.tvValueEmailEdit.setText(email)
        profileBinding.tvValuePhoneEdit.setText(nohp)
        Glide.with(this@ProfileActivity)
            .load(fotoPath)
            .placeholder(R.drawable.ic_profile)
            .error(R.drawable.ic_profile)
            .into(profileBinding.imgProfile)

        profileBinding.btnBack.setOnClickListener {
            onBackPressed()
        }

        profileBinding.btnSave.setOnClickListener {
            profileBinding.btnSave.startAnimation()
            onBackPressed()
        }

        profileBinding.btnChangeImg.setOnClickListener {
            ImagePicker.with(this)
                .cropSquare()
                .compress(1024)
                .maxResultSize(720, 720)
                .galleryMimeTypes(  //Exclude gif images
                    mimeTypes = arrayOf(
                        "image/png",
                        "image/jpg",
                        "image/jpeg"
                    )
                )
                .start { resultCode, data ->
                    when (resultCode) {
                        Activity.RESULT_OK -> {
                            val fileUri = data?.data
                            this.photoUri = fileUri
                            profileBinding.imgProfile.setImageURI(fileUri)
                            ImagePicker.getFile(data)
                            ImagePicker.getFilePath(data).toString()
                        }
                        ImagePicker.RESULT_ERROR -> {
                            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }

        profileBinding.btnSave.setOnClickListener {
            if (validate()) {
                profileBinding.btnSave.startAnimation()
                val idadmin = myPreferences.getValue(Constants.USER_ID).toString()
                    .toRequestBody(MultipartBody.FORM)
                val adminName = profileBinding.tvValueNameEdit.text.toString()
                    .toRequestBody(MultipartBody.FORM)
                val adminEmail = profileBinding.tvValueEmailEdit.text.toString()
                    .toRequestBody(MultipartBody.FORM)
                val adminPhone = profileBinding.tvValuePhoneEdit.text.toString()
                    .toRequestBody(MultipartBody.FORM)
                val tokenAuth = myPreferences.getValue(Constants.TokenAuth).toString()
                var photo: MultipartBody.Part? = null

                photoUri?.let {
                    val file = FileUtils.getFile(this, photoUri)
                    val requestBodyPhoto = file.asRequestBody(contentResolver.getType(it).toString().toMediaTypeOrNull())
                    photo = MultipartBody.Part.createFormData("filefoto", file.name, requestBodyPhoto)
                }

                val service = RetrofitClient().apiRequest().create(DataService::class.java)
                service.editProfile(
                    idadmin,
                    adminName,
                    adminEmail,
                    adminPhone,
                    photo,
                    "Bearer $tokenAuth"
                )
                    .enqueue(object : Callback<DefaultResponse> {
                        override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                            if (response.isSuccessful) {
                                if (response.body()!!.status == "success") {
                                    myPreferences.setValue(Constants.USER_NAMA, profileBinding.tvValueNameEdit.text.toString())
                                    myPreferences.setValue(Constants.USER_EMAIL, profileBinding.tvValueEmailEdit.text.toString())
                                    myPreferences.setValue(Constants.USER_NOHP, profileBinding.tvValuePhoneEdit.text.toString())
                                    val idAdmin = myPreferences.getValue(Constants.USER_ID).toString()
                                    getUserFoto(idAdmin)
                                } else {
                                    profileBinding.btnSave.endAnimation()
                                    Toasty.error(this@ProfileActivity, response.message(), Toasty.LENGTH_LONG).show()
                                }
                            } else {
                                Toasty.error(this@ProfileActivity, response.message(), Toasty.LENGTH_LONG).show()
                            }
                        }

                        override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                            profileBinding.btnSave.endAnimation()
                            Toasty.error(this@ProfileActivity, t.message.toString(), Toasty.LENGTH_LONG).show()
                        }

                    })
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ProfileActivity, SettingActivity::class.java))
        finish()
    }

    private fun validate(): Boolean {
        when {
            profileBinding.tvValueNameEdit.text.toString() == "" -> {
                profileBinding.tvValueNameEdit.error = getString(R.string.name_cant_empty)
                profileBinding.tvValueNameEdit.requestFocus()
                return false
            }
            profileBinding.tvValueEmailEdit.text.toString() == "" -> {
                profileBinding.tvValueEmailEdit.error = getString(R.string.email_cant_empty)
                profileBinding.tvValueEmailEdit.requestFocus()
                return false
            }
            profileBinding.tvValuePhoneEdit.text.toString() == "" -> {
                profileBinding.tvValuePhoneEdit.error = getString(R.string.phone_cant_empty)
                profileBinding.tvValuePhoneEdit.requestFocus()
                return false
            }
            else -> return true
        }
    }

    private fun getUserFoto(idadmin: String) {
        val service = RetrofitClient().apiRequest().create(AuthService::class.java)
        service.getUserFoto(idadmin).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        myPreferences.setValue(Constants.FOTO_PATH, response.body()!!.data[0].foto_path)
                        Toasty.success(this@ProfileActivity, "Berhasil mengedit profil", Toasty.LENGTH_LONG).show()
                        startActivity(Intent(this@ProfileActivity, MainActivity::class.java))
                        finish()
                    } else {
                        profileBinding.btnSave.endAnimation()
                        Toasty.error(this@ProfileActivity, "jamal", Toasty.LENGTH_LONG).show()
                    }
                } else {
                    Toasty.error(this@ProfileActivity, response.message(), Toasty.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toasty.error(this@ProfileActivity, R.string.try_again, Toasty.LENGTH_LONG).show()
            }

        })
    }
}