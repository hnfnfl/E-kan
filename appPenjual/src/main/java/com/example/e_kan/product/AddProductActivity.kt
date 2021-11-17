package com.example.e_kan.product

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_kan.databinding.ActivityAddProductBinding
import com.example.e_kan.retrofit.DataService
import com.example.e_kan.retrofit.RetrofitClient
import com.example.e_kan.retrofit.response.DefaultResponse
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

class AddProductActivity : AppCompatActivity() {

    private lateinit var addProductBinding: ActivityAddProductBinding
    private lateinit var myPreferences: MySharedPreferences
    var photoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addProductBinding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(addProductBinding.root)

        myPreferences = MySharedPreferences(this@AddProductActivity)

        addProductBinding.btnBack.setOnClickListener {
            onBackPressed()
            finish()
        }

        addProductBinding.btnAddProduct.setOnClickListener {
            addProductBinding.btnAddProduct.startAnimation()
            startActivity(Intent(this@AddProductActivity, ListProductActivity::class.java))
            finish()
        }

        addProductBinding.btnChangeImg.setOnClickListener {
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
                            addProductBinding.imgFish.setImageURI(fileUri)
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

        addProductBinding.btnAddProduct.setOnClickListener {
            if (validate()) {
                addProductBinding.btnAddProduct.startAnimation()
                val idpenjual = myPreferences.getValue(Constants.USER_ID).toString()
                    .toRequestBody(MultipartBody.FORM)
                val productName = addProductBinding.tvValueProductName.text.toString()
                    .toRequestBody(MultipartBody.FORM)
                val productDesc = addProductBinding.tvValueProductDesc.text.toString()
                    .toRequestBody(MultipartBody.FORM)
                val productPrice = addProductBinding.tvValueProductPrice.text.toString()
                    .toRequestBody(MultipartBody.FORM)
                val productWeight = addProductBinding.tvValueProductWeight.text.toString()
                    .toRequestBody(MultipartBody.FORM)
                val productStock = addProductBinding.tvValueProductStock.text.toString()
                    .toRequestBody(MultipartBody.FORM)
                val tokenAuth = myPreferences.getValue(Constants.TokenAuth).toString()

                var photo: MultipartBody.Part? = null
                photoUri?.let {
                    val file = FileUtils.getFile(this, photoUri)
                    val requestBodyPhoto = file.asRequestBody(contentResolver.getType(it).toString().toMediaTypeOrNull())
                    photo = MultipartBody.Part.createFormData("filefoto", file.name, requestBodyPhoto)
                }

                val service = RetrofitClient().apiRequest().create(DataService::class.java)
                service.addProduct(
                    idpenjual,
                    productName,
                    productDesc,
                    productPrice,
                    productWeight,
                    productStock,
                    photo,
                    "Bearer $tokenAuth"
                )
                    .enqueue(object : Callback<DefaultResponse> {
                        override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                            if (response.isSuccessful) {
                                if (response.body()!!.status == "success") {
                                    Toasty.success(this@AddProductActivity, "Berhasil menambahkan produk", Toasty.LENGTH_LONG).show()
                                    startActivity(Intent(this@AddProductActivity, ListProductActivity::class.java))
                                    finish()
                                } else {
                                    addProductBinding.btnAddProduct.endAnimation()
                                    Toasty.error(this@AddProductActivity, response.message(), Toasty.LENGTH_LONG).show()
                                }
                            }
                        }

                        override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                            addProductBinding.btnAddProduct.endAnimation()
                            Toasty.error(this@AddProductActivity, t.message.toString(), Toasty.LENGTH_LONG).show()
                        }

                    })
            }
        }
    }

    private fun validate(): Boolean {
        when {
            addProductBinding.tvValueProductName.text.toString() == "" -> {
                addProductBinding.tvValueProductName.error = "Nama ikan tidak boleh kosong"
                addProductBinding.tvValueProductName.requestFocus()
                return false
            }
            addProductBinding.tvValueProductDesc.text.toString() == "" -> {
                addProductBinding.tvValueProductDesc.error = "Deskripsi produk tidak boleh kosong"
                addProductBinding.tvValueProductDesc.requestFocus()
                return false
            }
            addProductBinding.tvValueProductPrice.text.toString() == "" -> {
                addProductBinding.tvValueProductPrice.error = "Harga tidak boleh kosong"
                addProductBinding.tvValueProductPrice.requestFocus()
                return false
            }
            addProductBinding.tvValueProductWeight.text.toString() == "" -> {
                addProductBinding.tvValueProductWeight.error = "Berat ikan tidak boleh kosong"
                addProductBinding.tvValueProductWeight.requestFocus()
                return false
            }
            addProductBinding.tvValueProductStock.text.toString() == "" -> {
                addProductBinding.tvValueProductStock.error = "Stok ikan tidak boleh kosong"
                addProductBinding.tvValueProductStock.requestFocus()
                return false
            }
            else -> return true
        }
    }
}