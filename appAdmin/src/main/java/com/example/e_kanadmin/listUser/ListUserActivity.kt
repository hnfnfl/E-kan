package com.example.e_kanadmin.listUser

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_kanadmin.MainActivity
import com.example.e_kanadmin.R
import com.example.e_kanadmin.databinding.ActivityListUserBinding
import com.example.e_kanadmin.retrofit.DataService
import com.example.e_kanadmin.retrofit.RetrofitClient
import com.example.e_kanadmin.retrofit.response.UserResponse
import com.example.e_kanadmin.utils.Constants
import com.example.e_kanadmin.utils.MySharedPreferences
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListUserActivity : AppCompatActivity() {

    private lateinit var listUserBinding: ActivityListUserBinding
    private lateinit var myPreferences: MySharedPreferences
    private lateinit var listUserAdapter: ListUserAdapter
    private var listUserSearch: ArrayList<UserEntity> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listUserBinding = ActivityListUserBinding.inflate(layoutInflater)
        setContentView(listUserBinding.root)

        myPreferences = MySharedPreferences(this@ListUserActivity)
        listUserAdapter = ListUserAdapter()

        listUserBinding.btnBack.setOnClickListener {
            onBackPressed()
        }

        val tokenAuth = getString(R.string.token_auth, myPreferences.getValue(Constants.TokenAuth).toString())

        listAllUser(tokenAuth)

        listUserBinding.svUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                listUserAdapter.filter.filter(query)
                return true
            }

        })
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ListUserActivity, MainActivity::class.java))
        finish()
    }

    private fun listAllUser(tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.getAllUser(tokenAuth).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        listUserBinding.loadingAnim.visibility = View.GONE
                        val listData = response.body()!!.data
                        listUserSearch = listData
                        listUserAdapter.setListUserItem(listUserSearch)
                        listUserAdapter.notifyDataSetChanged()

                        with(listUserBinding.rvUserList) {
                            layoutManager = LinearLayoutManager(this@ListUserActivity)
                            itemAnimator = DefaultItemAnimator()
                            setHasFixedSize(true)
                            adapter = listUserAdapter
                        }
                    }
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Toasty.error(this@ListUserActivity, R.string.try_again, Toasty.LENGTH_LONG).show()
            }
        })
    }
}