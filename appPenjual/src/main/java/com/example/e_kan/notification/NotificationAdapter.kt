package com.example.e_kan.notification

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.e_kan.R
import com.example.e_kan.databinding.ItemNotificationBinding
import com.example.e_kan.databinding.ItemOrderListBinding


class NotificationAdapter : RecyclerView.Adapter<NotificationAdapter.NotifItemHolder>() {

    private var notifList = ArrayList<NotifEntity>()

    fun setListVendorItem(listNotifItem: List<NotifEntity>?) {
        if (listNotifItem == null) return
        this.notifList.clear()
        this.notifList.addAll(listNotifItem)
        notifyDataSetChanged()
    }

    class NotifItemHolder(private val binding: ItemNotificationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(orderList: NotifEntity) {
            with(binding) {
                tvNotifBody.text = orderList.title
                tvNotifTime.text = orderList.timestamp
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifItemHolder {
        val itemNotifBinding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotifItemHolder(itemNotifBinding)
    }

    override fun onBindViewHolder(holder: NotifItemHolder, position: Int) {
        val notifItem = notifList[position]
        holder.bind(notifItem)
    }

    override fun getItemCount(): Int {
        return notifList.size
    }

}



