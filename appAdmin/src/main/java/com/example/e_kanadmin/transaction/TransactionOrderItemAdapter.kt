package com.example.e_kanadmin.transaction

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_kanadmin.R
import com.example.e_kanadmin.databinding.ItemOrderItemBinding
import java.text.DecimalFormat

class TransactionOrderItemAdapter : RecyclerView.Adapter<TransactionOrderItemAdapter.OrderItemHolder>() {

    private var orderListItem = ArrayList<OrderItemEntity>()

    fun setOrderItem(listOrderItem: List<OrderItemEntity>?) {
        if (listOrderItem == null) return
        this.orderListItem.clear()
        this.orderListItem.addAll(listOrderItem)
        notifyDataSetChanged()
    }

    class OrderItemHolder(private val binding: ItemOrderItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(orderItem: OrderItemEntity) {
            with(binding) {
                tvFishName.text = orderItem.produk
                val numbering = DecimalFormat("#,###").format(orderItem.harga?.toInt())
                tvFishPrice.text = itemView.context.getString(R.string.fish_detail_price, numbering)
                tvFishQty.text = itemView.context.getString(R.string.fish_detail_qty, orderItem.jumlah)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItemHolder {
        val itemOrderBinding = ItemOrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderItemHolder(itemOrderBinding)
    }

    override fun onBindViewHolder(holder: OrderItemHolder, position: Int) {
        val orderItem = orderListItem[position]
        holder.bind(orderItem)
    }

    override fun getItemCount(): Int {
        return orderListItem.size
    }

}



