package com.example.e_kan.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_kan.R
import com.example.e_kan.databinding.ItemProductTopFiveBinding
import java.text.DecimalFormat
import kotlin.collections.ArrayList

class ListTopFiveProductAdapter : RecyclerView.Adapter<ListTopFiveProductAdapter.ProductItemHolder>() {

    private var listVendorItem = ArrayList<ProductEntity>()
    private var listVendorItemFilter = ArrayList<ProductEntity>()


    fun setListTopFiveVendorItem(listVendorItem: List<ProductEntity>?) {
        if (listVendorItem == null) return
        this.listVendorItem.clear()
        this.listVendorItem.addAll(listVendorItem)

        this.listVendorItemFilter = listVendorItem as ArrayList<ProductEntity>
        notifyDataSetChanged()
    }

    class ProductItemHolder(private val binding: ItemProductTopFiveBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(productItem: ProductEntity) {
            with(binding) {
                tvFishName.text = productItem.nama_produk
                val numbering = DecimalFormat("#,###")
                tvFishPrice.text = numbering.format(productItem.harga.toInt())
                tvFishWeight.text = itemView.context.getString(R.string.fish_weight, productItem.berat)
                tvFishStock.text = productItem.stok
                tvFishSold.text = productItem.terjual
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItemHolder {
        val itemProductTopFiveBinding = ItemProductTopFiveBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductItemHolder(itemProductTopFiveBinding)
    }

    override fun onBindViewHolder(holder: ProductItemHolder, position: Int) {
        val vendorItem = listVendorItem[position]
        holder.bind(vendorItem)
    }

    override fun getItemCount(): Int {
        return listVendorItem.size
    }
}



