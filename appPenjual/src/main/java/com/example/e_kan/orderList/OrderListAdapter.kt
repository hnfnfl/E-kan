package com.example.e_kan.orderList

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_kan.databinding.ItemOrderListBinding
import java.util.*
import kotlin.collections.ArrayList


class OrderListAdapter : RecyclerView.Adapter<OrderListAdapter.ProductItemHolder>() {

    private var orderList = ArrayList<OrderEntity>()

    fun setListVendorItem(listVendorItem: List<OrderEntity>?) {
        if (listVendorItem == null) return
        this.orderList.clear()
        this.orderList.addAll(listVendorItem)
        notifyDataSetChanged()
    }

    class ProductItemHolder(private val binding: ItemOrderListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(orderList: OrderEntity) {
            val bundle = Bundle()
            bundle.putSerializable("pesanan", orderList.pesanan)

            with(binding) {
                tvUserName.text = orderList.nama
                tvOrderTime.text = orderList.timestamp
                tvOrderId.text = orderList.kode_pesanan
                tvOrderQty.text = orderList.pesanan.size.toString()
                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, OrderDetailActivity::class.java)
                        .apply {
                            putExtra(OrderDetailActivity.kodePesanan, orderList.kode_pesanan)
                            putExtra(OrderDetailActivity.namaPemesan, orderList.nama)
                            putExtra(OrderDetailActivity.tgl, orderList.timestamp)
                            putExtra(OrderDetailActivity.catatan, orderList.catatan)
                        }
                    intent.putExtra("pesanan", orderList.pesanan)
                    itemView.context.startActivity(intent)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItemHolder {
        val itemOrderBinding = ItemOrderListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductItemHolder(itemOrderBinding)
    }

    override fun onBindViewHolder(holder: ProductItemHolder, position: Int) {
        val vendorItem = orderList[position]
        holder.bind(vendorItem)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

}



