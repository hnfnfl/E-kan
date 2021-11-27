package com.example.e_kanadmin.listProduct

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_kanadmin.MainActivity
import com.example.e_kanadmin.R
import com.example.e_kanadmin.databinding.ActivityListProductBinding
import com.example.e_kanadmin.retrofit.DataService
import com.example.e_kanadmin.retrofit.RetrofitClient
import com.example.e_kanadmin.retrofit.response.ProductResponse
import com.example.e_kanadmin.utils.Constants
import com.example.e_kanadmin.utils.MySharedPreferences
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
        listProductAdapter = ListProductAdapter()

        listProductBinding.btnBack.setOnClickListener {
            onBackPressed()
        }

        val tokenAuth = myPreferences.getValue(Constants.TokenAuth).toString()

        getAllProduct(tokenAuth)

        listProductBinding.svUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                listProductAdapter.filter.filter(query)
                return true
            }

        })
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ListProductActivity, MainActivity::class.java))
        finish()
    }

    private fun getAllProduct(tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.getAllProduct("Bearer $tokenAuth").enqueue(object : Callback<ProductResponse> {
            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        listProductBinding.loadingAnim.visibility = View.GONE
                        val listData = response.body()!!.data
                        listProductSearch = listData
                        listProductAdapter.setListProductItem(listProductSearch)
                        listProductAdapter.notifyDataSetChanged()

                        with(listProductBinding.rvUserList) {
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