package com.example.e_kanadmin.listProduct

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.e_kanadmin.databinding.ItemListProductBinding
import com.example.e_kanadmin.databinding.ItemListSellerBinding
import java.util.*
import kotlin.collections.ArrayList

class ListProductAdapter : RecyclerView.Adapter<ListProductAdapter.ProductItemHolder>(), Filterable {

    private var listProduct = ArrayList<ProductEntity>()
    private var listProductFilter = ArrayList<ProductEntity>()

    fun setListProductItem(listSellerItem: List<ProductEntity>?) {
        if (listSellerItem == null) return
        this.listProduct.clear()
        this.listProduct.addAll(listSellerItem)
        this.listProductFilter = listSellerItem as ArrayList<ProductEntity>
        notifyDataSetChanged()
    }

    class ProductItemHolder(private val binding: ItemListProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listSellerItem: ProductEntity) {
            with(binding) {

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItemHolder {
        val itemProductBinding = ItemListProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductItemHolder(itemProductBinding)
    }

    override fun onBindViewHolder(holder: ProductItemHolder, position: Int) {
        val userItem = listProduct[position]
        holder.bind(userItem)
    }

    override fun getItemCount(): Int {
        return listProduct.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                val filterResults = FilterResults()

                if (constraint == null || constraint.length < 0) {
                    filterResults.count = listProductFilter.size
                    filterResults.values = listProductFilter
                } else {
                    val charSearch = constraint.toString()

                    val resultList = ArrayList<ProductEntity>()

                    for (row in listProductFilter) {
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
                listProduct = results?.values as ArrayList<ProductEntity>
                notifyDataSetChanged()
            }

        }
    }
}



