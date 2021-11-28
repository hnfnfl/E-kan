package com.example.e_kan

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_kan.databinding.ActivityMainBinding
import com.example.e_kan.notification.NotificationActivity
import com.example.e_kan.orderList.OrderListActivity
import com.example.e_kan.product.ListProductActivity
import com.example.e_kan.product.ListTopFiveProductAdapter
import com.example.e_kan.product.ProductEntity
import com.example.e_kan.retrofit.AuthService
import com.example.e_kan.retrofit.DataService
import com.example.e_kan.retrofit.RetrofitClient
import com.example.e_kan.retrofit.response.ChartResponse
import com.example.e_kan.retrofit.response.DefaultResponse
import com.example.e_kan.retrofit.response.LoginResponse
import com.example.e_kan.retrofit.response.ProductResponse
import com.example.e_kan.settings.SettingActivity
import com.example.e_kan.settings.profile.ProfileActivity
import com.example.e_kan.utils.AxisDateFormatter
import com.example.e_kan.utils.Constants
import com.example.e_kan.utils.MySharedPreferences
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import es.dmoral.toasty.Toasty
import nl.bryanderidder.themedtogglebuttongroup.ThemedButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var myPreferences: MySharedPreferences
    private lateinit var listTopFiveAdapter: ListTopFiveProductAdapter
    private var listTopFiveProduct: ArrayList<ProductEntity> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        myPreferences = MySharedPreferences(this@MainActivity)
        listTopFiveAdapter = ListTopFiveProductAdapter()

        mainBinding.llProduct.setOnClickListener {
            startActivity(Intent(this@MainActivity, ListProductActivity::class.java))
            finish()
        }

        mainBinding.llOrderHistory.setOnClickListener {
            startActivity(Intent(this@MainActivity, OrderListActivity::class.java))
            finish()
        }

        mainBinding.llSetting.setOnClickListener {
            startActivity(Intent(this@MainActivity, SettingActivity::class.java))
            finish()
        }

        mainBinding.btnProfile.setOnClickListener {
            startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
            finish()
        }

        mainBinding.btnNotif.setOnClickListener {
            startActivity(Intent(this@MainActivity, NotificationActivity::class.java))
            finish()
        }

        val idpenjual = myPreferences.getValue(Constants.USER_ID).toString()
        val tokenAuth = getString(R.string.token_auth, myPreferences.getValue(Constants.TokenAuth).toString())

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val deviceToken = task.result
            insertToken(idpenjual, deviceToken.toString())
        })
        refreshAuthToken(idpenjual)

        val themedButtonGroup = mainBinding.time
        themedButtonGroup.setOnSelectListener { button: ThemedButton ->
            if (button.text == getString(R.string.last_month)) {
                getTimeChart(idpenjual, "MONTH", tokenAuth)
            } else {
                getTimeChart(idpenjual, "YEAR", tokenAuth)
            }
        }

        listTopFiveProduct(idpenjual, tokenAuth)
    }

    private fun insertToken(idpenjual: String, device_token: String) {
        val service = RetrofitClient().apiRequest().create(AuthService::class.java)
        service.addToken(idpenjual, device_token).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                    }
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toasty.error(this@MainActivity, R.string.try_again, Toasty.LENGTH_LONG).show()
            }
        })
    }

    private fun refreshAuthToken(idpenjual: String) {
        val service = RetrofitClient().apiRequest().create(AuthService::class.java)
        service.refreshAuthToken(idpenjual).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    myPreferences.setValue(Constants.TokenAuth, response.body()!!.tokenAuth)
                    if (response.body()!!.status == "blocked") {
                        mainBinding.llBlockedWarning.visibility = View.VISIBLE
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toasty.error(this@MainActivity, R.string.try_again, Toasty.LENGTH_LONG).show()
            }
        })
    }

    private fun listTopFiveProduct(idpenjual: String, tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.getTopFiveProduct(idpenjual, tokenAuth).enqueue(object : Callback<ProductResponse> {
            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        mainBinding.loadingAnim.visibility = View.GONE
                        val listData = response.body()!!.data
                        listTopFiveProduct = listData
                        listTopFiveAdapter.setListTopFiveVendorItem(listTopFiveProduct)
                        listTopFiveAdapter.notifyDataSetChanged()

                        with(mainBinding.rvTopFive) {
                            layoutManager = LinearLayoutManager(this@MainActivity)
                            itemAnimator = DefaultItemAnimator()
                            setHasFixedSize(true)
                            adapter = listTopFiveAdapter
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                Toasty.error(this@MainActivity, R.string.try_again, Toasty.LENGTH_LONG).show()
            }
        })
    }

    private fun getTimeChart(idpenjual: String, time: String, tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.getTimeChart(idpenjual, time, tokenAuth).enqueue(object : Callback<ChartResponse> {
            override fun onResponse(call: Call<ChartResponse>, response: Response<ChartResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        val chart = mainBinding.timeChart
                        chart.visibility = View.VISIBLE
                        val listData = response.body()!!.data
                        var i = 0
                        val entries = ArrayList<BarEntry>()
                        val labels = ArrayList<String>()

                        while (i < listData.size) {
                            val stepX = 1F * i
                            entries.add(BarEntry(stepX, listData[i].pesanan.toFloat()))
                            labels.add(listData[i].tglPesan)
                            i++
                        }
                        val barDataSet = BarDataSet(entries, "")
                        val data = BarData(barDataSet)

                        chart.data = data
                        chart.setTouchEnabled(true)
                        chart.isDragEnabled
                        chart.animateXY(100, 500)
                        chart.description.isEnabled = false
                        chart.legend.isEnabled = false
//                        chart.setMaxVisibleValueCount(40) // if more than 60 entries are displayed in the chart, no values will be drawn

                        //hide grid lines
                        chart.axisLeft.setDrawGridLines(false)
                        chart.xAxis.setDrawGridLines(false)
                        chart.xAxis.setDrawAxisLine(false)
                        chart.invalidate()

                        val tanggal = AxisDateFormatter(labels.toArray(arrayOfNulls<String>(labels.size)))
                        val xAxis = chart.xAxis
                        xAxis.position = XAxisPosition.BOTTOM
                        xAxis.setDrawGridLines(false)
                        xAxis.granularity = 1f // only intervals of 1 day
                        xAxis.labelCount = 7
                        xAxis.valueFormatter = tanggal
                    }
                }
            }

            override fun onFailure(call: Call<ChartResponse>, t: Throwable) {
                Toasty.error(this@MainActivity, R.string.try_again, Toasty.LENGTH_LONG).show()
            }

        })
    }
}