package com.example.e_kan.orderList

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_kan.R
import com.example.e_kan.databinding.ActivityOrderDetailBinding
import com.example.e_kan.retrofit.DataService
import com.example.e_kan.retrofit.RetrofitClient
import com.example.e_kan.retrofit.response.DefaultResponse
import com.example.e_kan.utils.Constants
import com.example.e_kan.utils.MySharedPreferences
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderDetailActivity : AppCompatActivity() {

    private lateinit var orderDetailBinding: ActivityOrderDetailBinding
    private lateinit var orderListItemAdapter: OrderListItemAdapter
    private lateinit var myPreferences: MySharedPreferences

    companion object {
        const val idpesanan = "idpesanan"
        const val kodePesanan = "kodePesanan"
        const val namaPemesan = "namaPemesan"
        const val tgl = "tgl"
        const val catatan = "catatan"
        const val alamat = "alamat"
        const val status = "status"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderDetailBinding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(orderDetailBinding.root)

        myPreferences = MySharedPreferences(this@OrderDetailActivity)

        val idpesanan = intent.getStringExtra(idpesanan).toString()
        val kodePesanan = intent.getStringExtra(kodePesanan).toString()
        val namaPemesan = intent.getStringExtra(namaPemesan).toString()
        val tgl = intent.getStringExtra(tgl).toString()
        val catatan = intent.getStringExtra(catatan).toString()
        val alamat = intent.getStringExtra(alamat).toString()
        val status = intent.getStringExtra(status).toString()
        val tokenAuth = myPreferences.getValue(Constants.TokenAuth).toString()

        orderDetailBinding.btnBack.setOnClickListener {
            onBackPressed()
        }

        orderDetailBinding.btnConfirm.setOnClickListener {
            orderDetailBinding.btnConfirm.startAnimation()
            val statusEdit = getString(R.string.processed)
            editStatus(idpesanan, statusEdit, tokenAuth)
        }

        orderDetailBinding.btnStockEmpty.setOnClickListener {
            orderDetailBinding.btnStockEmpty.startAnimation()
            val statusEdit = getString(R.string.empty_stock)
            editStatus(idpesanan, statusEdit, tokenAuth)
        }

        orderDetailBinding.btnSent.setOnClickListener {
            orderDetailBinding.btnSent.startAnimation()
            val statusEdit = getString(R.string.sent)
            editStatus(idpesanan, statusEdit, tokenAuth)
        }

        orderListItemAdapter = OrderListItemAdapter()
        orderDetailBinding.tvKodePesanan.text = kodePesanan
        orderDetailBinding.tvDate.text = tgl
        orderDetailBinding.tvUserName.text = namaPemesan
        orderDetailBinding.tvNote.text = catatan
        orderDetailBinding.tvDetailAddress.text = alamat
        if (status == getString(R.string.unprocessed)) {
            orderDetailBinding.btnConfirm.visibility = View.VISIBLE
            orderDetailBinding.btnStockEmpty.visibility = View.VISIBLE
        } else if (status == getString(R.string.processed)) {
            orderDetailBinding.btnSent.visibility = View.VISIBLE
        }

        val list = intent.getParcelableArrayListExtra<OrderItemEntity>("pesanan") as ArrayList<OrderItemEntity>
        orderListItemAdapter.setOrderItem(list)
        orderListItemAdapter.notifyDataSetChanged()

        with(orderDetailBinding.rvOrderItem) {
            layoutManager = LinearLayoutManager(this@OrderDetailActivity)
            itemAnimator = DefaultItemAnimator()
            setHasFixedSize(true)
            adapter = orderListItemAdapter
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@OrderDetailActivity, OrderListActivity::class.java))
        finish()
    }

    private fun editStatus(idpenjual: String, status: String, tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.editStatus(idpenjual, status, "Bearer $tokenAuth").enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        startActivity(Intent(this@OrderDetailActivity, OrderListActivity::class.java))
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toasty.error(this@OrderDetailActivity, R.string.try_again, Toasty.LENGTH_LONG).show()
            }
        })
    }
}