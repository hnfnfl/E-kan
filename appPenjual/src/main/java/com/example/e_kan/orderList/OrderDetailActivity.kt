package com.example.e_kan.orderList

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_kan.databinding.ActivityOrderDetailBinding

class OrderDetailActivity : AppCompatActivity() {

    private lateinit var orderDetailBinding: ActivityOrderDetailBinding
    private lateinit var orderListItemAdapter: OrderListItemAdapter
    private var itemList: ArrayList<OrderItemEntity> = arrayListOf()

    companion object {
        const val kodePesanan = "kodePesanan"
        const val namaPemesan = "namaPemesan"
        const val tgl = "tgl"
        const val catatan = "catatan"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderDetailBinding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(orderDetailBinding.root)

        val kodePesanan = intent.getStringExtra(kodePesanan)
        val namaPemesan = intent.getStringExtra(namaPemesan)
        val tgl = intent.getStringExtra(tgl)
        val catatan = intent.getStringExtra(catatan)

        orderDetailBinding.tvKodePesanan.text = kodePesanan.toString()
        orderDetailBinding.tvDate.text = tgl.toString()
        orderDetailBinding.tvUserName.text = namaPemesan.toString()
        orderDetailBinding.tvNote.text = catatan.toString()

        val list = intent.getSerializableExtra("pesanan") as ArrayList<OrderItemEntity>
        itemList = list
        orderListItemAdapter.setOrderItem(itemList)
        orderListItemAdapter.notifyDataSetChanged()

        with(orderDetailBinding.rvOrderItem){
            layoutManager = LinearLayoutManager(this@OrderDetailActivity)
            itemAnimator = DefaultItemAnimator()
            setHasFixedSize(true)
            adapter = orderListItemAdapter
        }
    }
}