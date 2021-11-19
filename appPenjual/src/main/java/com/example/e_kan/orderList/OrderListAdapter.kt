package com.example.e_kan.orderList

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.e_kan.R
import com.example.e_kan.databinding.ItemOrderListBinding


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

            with(binding) {
                tvUserName.text = orderList.nama
                tvOrderTime.text = orderList.timestamp
                tvOrderId.text = orderList.kode_pesanan
                tvOrderQty.text = orderList.pesanan.size.toString()
                val status = orderList.status
                tvStatus.text = status
                when (status) {
                    itemView.context.getString(R.string.unprocessed) -> {
                        tvStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.md_red_400))
                    }
                    itemView.context.getString(R.string.processed) -> {
                        tvStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.md_indigo_400))
                    }
                    itemView.context.getString(R.string.sent) -> {
                        tvStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.md_blue_400))
                    }
                    itemView.context.getString(R.string.done) -> {
                        tvStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.md_green_400))
                    }
                }
                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, OrderDetailActivity::class.java)
                        .apply {
                            putExtra(OrderDetailActivity.idpesanan, orderList.idpesanan)
                            putExtra(OrderDetailActivity.kodePesanan, orderList.kode_pesanan)
                            putExtra(OrderDetailActivity.namaPemesan, orderList.nama)
                            putExtra(OrderDetailActivity.tgl, orderList.timestamp)
                            putExtra(OrderDetailActivity.catatan, orderList.catatan)
                            putExtra(OrderDetailActivity.alamat, orderList.alamat_antar)
                            putExtra(OrderDetailActivity.status, orderList.status)
                        }
                    intent.putExtra("pesanan", orderList.pesanan)
                    itemView.context.startActivity(intent)
                    (itemView.context as Activity).finish()
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



