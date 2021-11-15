package com.example.e_kan.product

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.e_kan.R
import com.example.e_kan.databinding.ItemProductBinding
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

class ListProductAdapter : RecyclerView.Adapter<ListProductAdapter.ProductItemHolder>(), Filterable {

    private var listVendorItem = ArrayList<ProductEntity>()
    private var listVendorItemFilter = ArrayList<ProductEntity>()

    fun setListVendorItem(listVendorItem: List<ProductEntity>?) {
        if (listVendorItem == null) return
        this.listVendorItem.clear()
        this.listVendorItem.addAll(listVendorItem)

        this.listVendorItemFilter = listVendorItem as ArrayList<ProductEntity>
        notifyDataSetChanged()
    }

    class ProductItemHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listProductItem: ProductEntity) {
            with(binding) {
                tvFishName.text = listProductItem.nama_produk
                val numbering = DecimalFormat("#,###")
                tvFishPrice.text = numbering.format(listProductItem.harga.toInt())
                tvFishWeight.text = itemView.context.getString(R.string.fish_weight, listProductItem.berat)
                tvFishStock.text = listProductItem.stok
                tvFishSold.text = listProductItem.terjual
                btnEditProduct.setOnClickListener {
                    itemView.context.startActivity(Intent(itemView.context, AddProductActivity::class.java))
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItemHolder {
        val itemProductBinding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductItemHolder(itemProductBinding)
    }

    override fun onBindViewHolder(holder: ProductItemHolder, position: Int) {
        val vendorItem = listVendorItem[position]
        holder.bind(vendorItem)
    }

    override fun getItemCount(): Int {
        return listVendorItem.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                val filterResults = FilterResults()

                if (constraint == null || constraint.length < 0) {
                    filterResults.count = listVendorItemFilter.size
                    filterResults.values = listVendorItemFilter
                } else {
                    val charSearch = constraint.toString()

                    val resultList = ArrayList<ProductEntity>()

                    for (row in listVendorItemFilter) {
                        if (row.nama_produk.toLowerCase(Locale.getDefault()).contains(charSearch.toLowerCase(Locale.getDefault()))) {
                            resultList.add(row)
                        }
                    }
                    filterResults.count = resultList.size
                    filterResults.values = resultList
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                listVendorItem = results?.values as ArrayList<ProductEntity>
                notifyDataSetChanged()
            }

        }
    }
}



