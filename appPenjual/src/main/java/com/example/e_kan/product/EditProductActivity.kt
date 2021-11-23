package com.example.e_kan.product

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.e_kan.R
import com.example.e_kan.databinding.ActivityEditProductBinding
import com.example.e_kan.retrofit.DataService
import com.example.e_kan.retrofit.RetrofitClient
import com.example.e_kan.retrofit.response.DefaultResponse
import com.example.e_kan.utils.Constants
import com.example.e_kan.utils.FileUtils
import com.example.e_kan.utils.MySharedPreferences
import com.github.dhaval2404.imagepicker.ImagePicker
import dev.shreyaspatil.MaterialDialog.MaterialDialog
import es.dmoral.toasty.Toasty
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class EditProductActivity : AppCompatActivity() {

    private lateinit var editProductBinding: ActivityEditProductBinding
    private lateinit var myPreferences: MySharedPreferences
    var photoUri: Uri? = null

    companion object {
        const val idproduk = "idproduk"
        const val nama = "nama"
        const val keterangan = "keterangan"
        const val harga = "harga"
        const val berat = "berat"
        const val stok = "stok"
        const val foto_path = "foto_path"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editProductBinding = ActivityEditProductBinding.inflate(layoutInflater)
        setContentView(editProductBinding.root)

        myPreferences = MySharedPreferences(this@EditProductActivity)

        val idproduk = intent.getStringExtra(idproduk).toString()
        val nama = intent.getStringExtra(nama).toString()
        val keterangan = intent.getStringExtra(keterangan).toString()
        val harga = intent.getStringExtra(harga).toString()
        val berat = intent.getStringExtra(berat).toString()
        val stok = intent.getStringExtra(stok).toString()
        val fotoPath = intent.getStringExtra(foto_path).toString()
        val tokenAuth = myPreferences.getValue(Constants.TokenAuth).toString()

        editProductBinding.tvValueProductName.setText(nama)
        editProductBinding.tvValueProductDesc.setText(keterangan)
        editProductBinding.tvValueProductPrice.setText(harga)
        editProductBinding.tvValueProductWeight.setText(berat)
        editProductBinding.tvValueProductStock.setText(stok)
        Glide.with(this@EditProductActivity)
            .load(fotoPath)
            .placeholder(R.drawable.ic_fish)
            .error(R.drawable.ic_fish)
            .into(editProductBinding.imgFish)

        editProductBinding.btnBack.setOnClickListener {
            onBackPressed()
        }

        editProductBinding.btnChangeImg.setOnClickListener {
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
                            editProductBinding.imgFish.setImageURI(fileUri)
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

        editProductBinding.btnEditProduct.setOnClickListener {
            if (validate()) {
                editProductBinding.btnEditProduct.startAnimation()
                val idProduk = idproduk
                val idMP = idProduk.toRequestBody(MultipartBody.FORM)

                val idpenjual = myPreferences.getValue(Constants.USER_ID).toString()
                val idpenjualMP = idpenjual.toRequestBody(MultipartBody.FORM)

                val productName = editProductBinding.tvValueProductName.text.toString()
                val nameMP = productName.toRequestBody(MultipartBody.FORM)

                val productDesc = editProductBinding.tvValueProductDesc.text.toString()
                val descMP = productDesc.toRequestBody(MultipartBody.FORM)

                val productPrice = editProductBinding.tvValueProductPrice.text.toString()
                val priceMP = productPrice.toRequestBody(MultipartBody.FORM)

                val productWeight = editProductBinding.tvValueProductWeight.text.toString()
                val weightMP = productWeight.toRequestBody(MultipartBody.FORM)

                val productStock = editProductBinding.tvValueProductStock.text.toString()
                val stockMP = productStock.toRequestBody(MultipartBody.FORM)

                var photo: MultipartBody.Part? = null
                photoUri?.let {
                    val file = FileUtils.getFile(this, photoUri)
                    val requestBodyPhoto = file.asRequestBody(contentResolver.getType(it).toString().toMediaTypeOrNull())
                    photo = MultipartBody.Part.createFormData("filefoto", file.name, requestBodyPhoto)
                }

                val service = RetrofitClient().apiRequest().create(DataService::class.java)
                service.editProduct(
                    idMP,
                    idpenjualMP,
                    nameMP,
                    descMP,
                    priceMP,
                    weightMP,
                    stockMP,
                    photo,
                    "Bearer $tokenAuth"
                )
                    .enqueue(object : Callback<DefaultResponse> {
                        override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                            if (response.isSuccessful) {
                                if (response.body()!!.status == "success") {
                                    Toasty.success(this@EditProductActivity, "Berhasil mengedit produk", Toasty.LENGTH_LONG).show()
                                    startActivity(Intent(this@EditProductActivity, ListProductActivity::class.java))
                                    finish()
                                } else {
                                    editProductBinding.btnEditProduct.endAnimation()
                                    Toasty.error(this@EditProductActivity, getString(R.string.try_again), Toasty.LENGTH_LONG).show()
                                }
                            }
                        }

                        override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                            editProductBinding.btnEditProduct.endAnimation()
                            Toasty.error(this@EditProductActivity, t.message.toString(), Toasty.LENGTH_LONG).show()
                        }

                    })
            }
        }

        editProductBinding.btnDeleteProduct.setOnClickListener {
            editProductBinding.btnDeleteProduct.startAnimation()
            val mDialog = MaterialDialog.Builder(this@EditProductActivity)
                .setTitle("Hapus Produk")
                .setMessage(getString(R.string.confirm_delete))
                .setCancelable(true)
                .setPositiveButton(
                    getString(R.string.no), R.drawable.ic_close
                ) { dialogInterface, _ ->
                    dialogInterface.dismiss()
                    editProductBinding.btnDeleteProduct.endAnimation()
                }
                .setNegativeButton(
                    getString(R.string.yes), R.drawable.ic_trash
                ) { dialogInterface, _ ->
                    deleteProduct(idproduk, tokenAuth)
                    dialogInterface.dismiss()
                }
                .build()
            // Show Dialog
            mDialog.show()
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@EditProductActivity, ListProductActivity::class.java))
        finish()
    }

    private fun deleteProduct(idproduk: String, tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.deleteProduct(idproduk, "Bearer $tokenAuth").enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        Toasty.success(this@EditProductActivity, "Produk berhasil dihapus", Toasty.LENGTH_LONG).show()
                        startActivity(Intent(this@EditProductActivity, ListProductActivity::class.java))
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toasty.error(this@EditProductActivity, R.string.try_again, Toasty.LENGTH_LONG).show()
            }
        })
    }

    private fun validate(): Boolean {
        when {
            editProductBinding.tvValueProductName.text.toString() == "" -> {
                editProductBinding.tvValueProductName.error = "Nama ikan tidak boleh kosong"
                editProductBinding.tvValueProductName.requestFocus()
                return false
            }
            editProductBinding.tvValueProductDesc.text.toString() == "" -> {
                editProductBinding.tvValueProductDesc.error = "Deskripsi produk tidak boleh kosong"
                editProductBinding.tvValueProductDesc.requestFocus()
                return false
            }
            editProductBinding.tvValueProductPrice.text.toString() == "" -> {
                editProductBinding.tvValueProductPrice.error = "Harga tidak boleh kosong"
                editProductBinding.tvValueProductPrice.requestFocus()
                return false
            }
            editProductBinding.tvValueProductWeight.text.toString() == "" -> {
                editProductBinding.tvValueProductWeight.error = "Berat ikan tidak boleh kosong"
                editProductBinding.tvValueProductWeight.requestFocus()
                return false
            }
            editProductBinding.tvValueProductStock.text.toString() == "" -> {
                editProductBinding.tvValueProductStock.error = "Stok ikan tidak boleh kosong"
                editProductBinding.tvValueProductStock.requestFocus()
                return false
            }
            else -> return true
        }
    }
}