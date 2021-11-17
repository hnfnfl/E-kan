package com.example.e_kan.settings.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.e_kan.MainActivity
import com.example.e_kan.R
import com.example.e_kan.databinding.ActivityProfileBinding
import com.example.e_kan.retrofit.AuthService
import com.example.e_kan.retrofit.DataService
import com.example.e_kan.retrofit.RetrofitClient
import com.example.e_kan.retrofit.response.DefaultResponse
import com.example.e_kan.retrofit.response.LoginResponse
import com.example.e_kan.utils.Constants
import com.example.e_kan.utils.FileUtils
import com.example.e_kan.utils.MySharedPreferences
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
        val namaToko = myPreferences.getValue(Constants.USER_NAMA_TOKO).toString()
        val alamat = myPreferences.getValue(Constants.USER_ALAMAT).toString()
        val email = myPreferences.getValue(Constants.USER_EMAIL).toString()
        val nohp = myPreferences.getValue(Constants.USER_NOHP).toString()
        val fotoPath = myPreferences.getValue(Constants.FOTO_PATH).toString()

        profileBinding.tvValueNameEdit.setText(nama)
        profileBinding.tvValueShopEdit.setText(namaToko)
        profileBinding.tvValueAddressEdit.setText(alamat)
        profileBinding.tvValueEmailEdit.setText(email)
        profileBinding.tvValuePhoneEdit.setText(nohp)
        Glide.with(this@ProfileActivity)
            .load(fotoPath)
            .placeholder(R.drawable.ic_profile)
            .error(R.drawable.ic_profile)
            .into(profileBinding.imgProfile)

        profileBinding.btnBack.setOnClickListener {
            onBackPressed()
            finish()
        }

        profileBinding.btnSave.setOnClickListener {
            profileBinding.btnSave.startAnimation()
            onBackPressed()
            finish()
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
                val idpenjual = myPreferences.getValue(Constants.USER_ID).toString()
                    .toRequestBody(MultipartBody.FORM)
                val sellerName = profileBinding.tvValueNameEdit.text.toString()
                    .toRequestBody(MultipartBody.FORM)
                val sellerShop = profileBinding.tvValueShopEdit.text.toString()
                    .toRequestBody(MultipartBody.FORM)
                val sellerAddress = profileBinding.tvValueAddressEdit.text.toString()
                    .toRequestBody(MultipartBody.FORM)
                val sellerEmail = profileBinding.tvValueEmailEdit.text.toString()
                    .toRequestBody(MultipartBody.FORM)
                val sellerPhone = profileBinding.tvValuePhoneEdit.text.toString()
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
                    idpenjual,
                    sellerName,
                    sellerShop,
                    sellerAddress,
                    sellerEmail,
                    sellerPhone,
                    photo,
                    "Bearer $tokenAuth"
                )
                    .enqueue(object : Callback<DefaultResponse> {
                        override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                            if (response.isSuccessful) {
                                if (response.body()!!.status == "success") {
                                    myPreferences.setValue(Constants.USER_NAMA, profileBinding.tvValueNameEdit.text.toString())
                                    myPreferences.setValue(Constants.USER_NAMA_TOKO, profileBinding.tvValueShopEdit.text.toString())
                                    myPreferences.setValue(Constants.USER_ALAMAT, profileBinding.tvValueAddressEdit.text.toString())
                                    myPreferences.setValue(Constants.USER_EMAIL, profileBinding.tvValueEmailEdit.text.toString())
                                    myPreferences.setValue(Constants.USER_NOHP, profileBinding.tvValuePhoneEdit.text.toString())
                                    val idpenjual = myPreferences.getValue(Constants.USER_ID).toString()
                                    getUserFoto(idpenjual)
                                    Toasty.success(this@ProfileActivity, "Berhasil mengedit profil", Toasty.LENGTH_LONG).show()
                                    startActivity(Intent(this@ProfileActivity, MainActivity::class.java))
                                    finish()
                                } else {
                                    profileBinding.btnSave.endAnimation()
                                    Toasty.error(this@ProfileActivity, response.message(), Toasty.LENGTH_LONG).show()
                                }
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

    private fun getUserFoto(idpenjual: String) {
        val service = RetrofitClient().apiRequest().create(AuthService::class.java)
        service.getUserFoto(idpenjual).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        myPreferences.setValue(Constants.FOTO_PATH, response.body()!!.data[0].foto_path)
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toasty.error(this@ProfileActivity, R.string.try_again, Toasty.LENGTH_LONG).show()
            }

        })
    }

    private fun validate(): Boolean {
        when {
            profileBinding.tvValueNameEdit.text.toString() == "" -> {
                profileBinding.tvValueNameEdit.error = "Nama tidak boleh kosong"
                profileBinding.tvValueNameEdit.requestFocus()
                return false
            }
            profileBinding.tvValueShopEdit.text.toString() == "" -> {
                profileBinding.tvValueShopEdit.error = "Nama toko tidak boleh kosong"
                profileBinding.tvValueShopEdit.requestFocus()
                return false
            }
            profileBinding.tvValueAddressEdit.text.toString() == "" -> {
                profileBinding.tvValueAddressEdit.error = "Alamat tidak boleh kosong"
                profileBinding.tvValueAddressEdit.requestFocus()
                return false
            }
            profileBinding.tvValueEmailEdit.text.toString() == "" -> {
                profileBinding.tvValueEmailEdit.error = "Email tidak boleh kosong"
                profileBinding.tvValueEmailEdit.requestFocus()
                return false
            }
            profileBinding.tvValuePhoneEdit.text.toString() == "" -> {
                profileBinding.tvValuePhoneEdit.error = "Nomor HP tidak boleh kosong"
                profileBinding.tvValuePhoneEdit.requestFocus()
                return false
            }
            else -> return true
        }
    }

}