package com.example.e_kan.notification

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_kan.R
import com.example.e_kan.databinding.ActivityNotificationBinding
import com.example.e_kan.retrofit.DataService
import com.example.e_kan.retrofit.RetrofitClient
import com.example.e_kan.retrofit.response.NotificationResponse
import com.example.e_kan.retrofit.response.OrderResponse
import com.example.e_kan.utils.Constants
import com.example.e_kan.utils.MySharedPreferences
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationActivity : AppCompatActivity() {

    private lateinit var notificationBinding: ActivityNotificationBinding
    private lateinit var myPreferences: MySharedPreferences
    private lateinit var notifAdapter: NotificationAdapter
    private var notifList: ArrayList<NotifEntity> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notificationBinding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(notificationBinding.root)

        myPreferences = MySharedPreferences(this@NotificationActivity)

        val idpenjual = myPreferences.getValue(Constants.USER_ID).toString()
        val tokenAuth = myPreferences.getValue(Constants.TokenAuth).toString()

        notificationBinding.btnBack.setOnClickListener {
            onBackPressed()
            finish()
        }

        notifAdapter = NotificationAdapter()
        notifList(idpenjual, tokenAuth)
    }

    private fun notifList(idpenjual: String, tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.getNotification(idpenjual, "Bearer $tokenAuth").enqueue(object : Callback<NotificationResponse> {
            override fun onResponse(call: Call<NotificationResponse>, response: Response<NotificationResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
//                        notificationBinding.loadingAnim.visibility = View.GONE
                        val listData = response.body()!!.data
                        notifList = listData
                        notifAdapter.setListVendorItem(notifList)
                        notifAdapter.notifyDataSetChanged()

                        with(notificationBinding.rvNotification) {
                            layoutManager = LinearLayoutManager(this@NotificationActivity)
                            itemAnimator = DefaultItemAnimator()
                            setHasFixedSize(true)
                            adapter = notifAdapter
                        }
                    } else {
                        notificationBinding.imgEmpty.visibility = View.VISIBLE
                    }
                }
            }

            override fun onFailure(call: Call<NotificationResponse>, t: Throwable) {
                Toasty.error(this@NotificationActivity, R.string.try_again, Toasty.LENGTH_LONG).show()
            }
        })
    }
}