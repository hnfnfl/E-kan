package com.example.e_kan.product

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.example.e_kan.databinding.ActivityAddProductBinding
import com.example.e_kan.utils.MySharedPreferences
import com.github.dhaval2404.imagepicker.ImagePicker
import java.io.File

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