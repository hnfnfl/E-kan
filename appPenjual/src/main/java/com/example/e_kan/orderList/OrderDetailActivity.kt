package com.example.e_kan.orderList

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_kan.R
import com.example.e_kan.databinding.ActivityOrderDetailBinding

class OrderDetailActivity : AppCompatActivity() {

    private lateinit var orderDetailBinding: ActivityOrderDetailBinding
    private lateinit var orderListItemAdapter: OrderListItemAdapter

    companion object {
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

        val kodePesanan = intent.getStringExtra(kodePesanan)
        val namaPemesan = intent.getStringExtra(namaPemesan)
        val tgl = intent.getStringExtra(tgl)
        val catatan = intent.getStringExtra(catatan)
        val alamat = intent.getStringExtra(alamat)
        val status = intent.getStringExtra(status)

        orderDetailBinding.btnBack.setOnClickListener {
            onBackPressed()
            finish()
        }

        orderDetailBinding.btnConfirm.setOnClickListener {
            orderDetailBinding.btnConfirm.startAnimation()
        }

        orderDetailBinding.btnStockEmpty.setOnClickListener {
            orderDetailBinding.btnStockEmpty.startAnimation()
        }

        orderDetailBinding.btnSent.setOnClickListener {
            orderDetailBinding.btnSent.startAnimation()
        }

        orderListItemAdapter = OrderListItemAdapter()
        orderDetailBinding.tvKodePesanan.text = kodePesanan.toString()
        orderDetailBinding.tvDate.text = tgl.toString()
        orderDetailBinding.tvUserName.text = namaPemesan.toString()
        orderDetailBinding.tvNote.text = catatan.toString()
        orderDetailBinding.tvDetailAddress.text = alamat.toString()
        if (status == getString(R.string.unprocessed)){
            orderDetailBinding.btnConfirm.visibility = View.VISIBLE
            orderDetailBinding.btnStockEmpty.visibility = View.VISIBLE
        } else if (status == getString(R.string.processed)){
            orderDetailBinding.btnSent.visibility = View.VISIBLE
        }

        val list = intent.getParcelableArrayListExtra<OrderItemEntity>("pesanan") as ArrayList<OrderItemEntity>
        orderListItemAdapter.setOrderItem(list)
        orderListItemAdapter.notifyDataSetChanged()

        with(orderDetailBinding.rvOrderItem){
            layoutManager = LinearLayoutManager(this@OrderDetailActivity)
            itemAnimator = DefaultItemAnimator()
            setHasFixedSize(true)
            adapter = orderListItemAdapter
        }
    }
}