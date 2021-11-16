package com.example.e_kan.product

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_kan.R
import com.example.e_kan.databinding.ActivityListProductBinding
import com.example.e_kan.retrofit.DataService
import com.example.e_kan.retrofit.RetrofitClient
import com.example.e_kan.retrofit.response.ProductResponse
import com.example.e_kan.utils.Constants
import com.example.e_kan.utils.MySharedPreferences
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListProductActivity : AppCompatActivity() {

    private lateinit var listProductBinding: ActivityListProductBinding
    private lateinit var myPreferences: MySharedPreferences
    private lateinit var listProductAdapter: ListProductAdapter
    private var listProductSearch: ArrayList<ProductEntity> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listProductBinding = ActivityListProductBinding.inflate(layoutInflater)
        setContentView(listProductBinding.root)

        myPreferences = MySharedPreferences(this@ListProductActivity)

        val idpenjual = myPreferences.getValue(Constants.USER_ID).toString()
        val tokenAuth = myPreferences.getValue(Constants.TokenAuth).toString()

        listProductBinding.btnBack.setOnClickListener {
            onBackPressed()
            finish()
        }

        listProductBinding.btnAddProduct.setOnClickListener {
            startActivity(Intent(this@ListProductActivity, AddProductActivity::class.java))
            finish()
        }

        listProductBinding.svProduct.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                listProductAdapter.filter.filter(query)
                return true
            }
        })

        listProductAdapter = ListProductAdapter()
        listProduct(idpenjual, tokenAuth)
    }

    private fun listProduct(idpenjual: String, tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.getAllProduct(idpenjual, "Bearer $tokenAuth").enqueue(object : Callback<ProductResponse> {
            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        listProductBinding.loadingAnim.visibility = View.GONE
                        val listData = response.body()!!.data
                        listProductSearch = listData
                        listProductAdapter.setListVendorItem(listProductSearch)
                        listProductAdapter.notifyDataSetChanged()

                        with(listProductBinding.rvProductList) {
                            layoutManager = LinearLayoutManager(this@ListProductActivity)
                            itemAnimator = DefaultItemAnimator()
                            setHasFixedSize(true)
                            adapter = listProductAdapter
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                Toasty.error(this@ListProductActivity, R.string.try_again, Toasty.LENGTH_LONG).show()
            }
        })

    }
}