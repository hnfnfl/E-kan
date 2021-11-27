package com.example.e_kanadmin.listSeller

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_kanadmin.MainActivity
import com.example.e_kanadmin.R
import com.example.e_kanadmin.databinding.ActivityListSellerBinding
import com.example.e_kanadmin.retrofit.DataService
import com.example.e_kanadmin.retrofit.RetrofitClient
import com.example.e_kanadmin.retrofit.response.SellerResponse
import com.example.e_kanadmin.retrofit.response.UserResponse
import com.example.e_kanadmin.utils.Constants
import com.example.e_kanadmin.utils.MySharedPreferences
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListSellerActivity : AppCompatActivity() {

    private lateinit var listSellerBinding: ActivityListSellerBinding
    private lateinit var myPreferences: MySharedPreferences
    private lateinit var listSellerAdapter: ListSellerAdapter
    private var listSellerSearch: ArrayList<SellerEntity> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listSellerBinding = ActivityListSellerBinding.inflate(layoutInflater)
        setContentView(listSellerBinding.root)

        myPreferences = MySharedPreferences(this@ListSellerActivity)
        listSellerAdapter = ListSellerAdapter()

        listSellerBinding.btnBack.setOnClickListener {
            onBackPressed()
        }

        val tokenAuth = getString(R.string.token_auth, myPreferences.getValue(Constants.TokenAuth).toString())

        listAllSeller(tokenAuth)

        listSellerBinding.svUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                listSellerAdapter.filter.filter(query)
                return true
            }

        })
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ListSellerActivity, MainActivity::class.java))
        finish()
    }

    private fun listAllSeller(tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.getAllSeller(tokenAuth).enqueue(object : Callback<SellerResponse> {
            override fun onResponse(call: Call<SellerResponse>, response: Response<SellerResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        listSellerBinding.loadingAnim.visibility = View.GONE
                        val listData = response.body()!!.data
                        listSellerSearch = listData
                        listSellerAdapter.setListSellerItem(listSellerSearch)
                        listSellerAdapter.notifyDataSetChanged()

                        with(listSellerBinding.rvSellerList) {
                            layoutManager = LinearLayoutManager(this@ListSellerActivity)
                            itemAnimator = DefaultItemAnimator()
                            setHasFixedSize(true)
                            adapter = listSellerAdapter
                        }
                    }
                }
            }

            override fun onFailure(call: Call<SellerResponse>, t: Throwable) {
                Toasty.error(this@ListSellerActivity, R.string.try_again, Toasty.LENGTH_LONG).show()
            }
        })
    }
}