package com.example.e_kanadmin.listProduct

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.e_kanadmin.R
import com.example.e_kanadmin.databinding.ItemListProductBinding
import java.text.DecimalFormat
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
        fun bind(listProductItem: ProductEntity) {
            with(binding) {
                tvProductName.text = listProductItem.nama_produk
                val numbering = DecimalFormat("#,###")
                val price = numbering.format(listProductItem.harga.toInt())
                tvFishPrice.text = itemView.context.getString(R.string.product_price, price)
                tvSellerShop.text = listProductItem.nama_toko
                Glide.with(itemView.context)
                    .load(listProductItem.foto_path)
                    .transform(CenterCrop())
                    .placeholder(R.drawable.ic_fish)
                    .error(R.drawable.ic_fish)
                    .into(imgProduct)

                if (listProductItem.status == "hidden") {
                    visibility.visibility = View.VISIBLE
                }

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, ProductDetailActivity::class.java)
                        .apply {
                            putExtra(ProductDetailActivity.idproduk, listProductItem.idproduk)
                            putExtra(ProductDetailActivity.nama_toko, listProductItem.nama_toko)
                            putExtra(ProductDetailActivity.nama_produk, listProductItem.nama_produk)
                            putExtra(ProductDetailActivity.keterangan, listProductItem.keterangan)
                            putExtra(ProductDetailActivity.harga, listProductItem.harga)
                            putExtra(ProductDetailActivity.statusProduk, listProductItem.status)
                            putExtra(ProductDetailActivity.foto_path, listProductItem.foto_path)
                        }
                    itemView.context.startActivity(intent)
                    (itemView.context as Activity).finish()
                }
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



