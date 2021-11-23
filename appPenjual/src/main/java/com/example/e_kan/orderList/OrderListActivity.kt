package com.example.e_kan.orderList

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_kan.MainActivity
import com.example.e_kan.R
import com.example.e_kan.databinding.ActivityOrderListBinding
import com.example.e_kan.retrofit.DataService
import com.example.e_kan.retrofit.RetrofitClient
import com.example.e_kan.retrofit.response.OrderResponse
import com.example.e_kan.utils.Constants
import com.example.e_kan.utils.MySharedPreferences
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderListActivity : AppCompatActivity() {

    private lateinit var orderListBinding: ActivityOrderListBinding
    private lateinit var myPreferences: MySharedPreferences
    private lateinit var orderListAdapter: OrderListAdapter
    private var orderList: ArrayList<OrderEntity> = arrayListOf()
    private var filterStatus: ArrayList<OrderEntity> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderListBinding = ActivityOrderListBinding.inflate(layoutInflater)
        setContentView(orderListBinding.root)

        myPreferences = MySharedPreferences(this@OrderListActivity)

        val idpenjual = myPreferences.getValue(Constants.USER_ID).toString()
        val tokenAuth = myPreferences.getValue(Constants.TokenAuth).toString()

        orderListBinding.btnBack.setOnClickListener {
            onBackPressed()
        }

        orderListAdapter = OrderListAdapter()
        orderList(idpenjual, tokenAuth)

        // Create an ArrayAdapter
        val array = resources.getStringArray(R.array.status_filter)
        val adapter = ArrayAdapter.createFromResource(this, R.array.status_filter, android.R.layout.simple_spinner_item)
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        orderListBinding.statusFilter.adapter = adapter
        orderListBinding.statusFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when (array[p2]) {
                    getString(R.string.show_all) -> {
                        filterStatus.clear()
                        orderListAdapter.setListVendorItem(orderList)
                    }
                    getString(R.string.unprocessed) -> {
                        filterStatus.clear()
                        orderList.forEach { orderList ->
                            if (orderList.status == getString(R.string.unprocessed)) {
                                filterStatus.add(orderList)
                            }
                        }
                        orderListAdapter.setListVendorItem(filterStatus)
                    }
                    getString(R.string.processed) -> {
                        filterStatus.clear()
                        orderList.forEach { orderList ->
                            if (orderList.status == getString(R.string.processed)) {
                                filterStatus.add(orderList)
                            }
                        }
                        orderListAdapter.setListVendorItem(filterStatus)
                    }
                    getString(R.string.sent) -> {
                        filterStatus.clear()
                        orderList.forEach { orderList ->
                            if (orderList.status == getString(R.string.sent)) {
                                filterStatus.add(orderList)
                            }
                        }
                        orderListAdapter.setListVendorItem(filterStatus)
                    }
                    getString(R.string.done) -> {
                        filterStatus.clear()
                        orderList.forEach { orderList ->
                            if (orderList.status == getString(R.string.done)) {
                                filterStatus.add(orderList)
                            }
                        }
                        orderListAdapter.setListVendorItem(filterStatus)
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }


    }

    private fun orderList(idpenjual: String, tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.getOrder(idpenjual, "Bearer $tokenAuth").enqueue(object : Callback<OrderResponse> {
            override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        orderListBinding.loadingAnim.visibility = View.GONE
                        val listData = response.body()!!.data
                        orderList = listData
                        orderListAdapter.setListVendorItem(orderList)
                        orderListAdapter.notifyDataSetChanged()

                        with(orderListBinding.rvOrderList) {
                            layoutManager = LinearLayoutManager(this@OrderListActivity)
                            itemAnimator = DefaultItemAnimator()
                            setHasFixedSize(true)
                            adapter = orderListAdapter
                        }
                    }
                }
            }

            override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                Toasty.error(this@OrderListActivity, R.string.try_again, Toasty.LENGTH_LONG).show()
            }
        })
    }

    override fun onBackPressed() {
        startActivity(Intent(this@OrderListActivity, MainActivity::class.java))
        finish()
    }
}