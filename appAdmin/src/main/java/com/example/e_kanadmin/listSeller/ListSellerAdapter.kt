package com.example.e_kanadmin.listSeller

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
import com.example.e_kanadmin.databinding.ItemListSellerBinding
import com.example.e_kanadmin.listUser.UserDetailActivity
import java.util.*
import kotlin.collections.ArrayList

class ListSellerAdapter : RecyclerView.Adapter<ListSellerAdapter.SellerItemHolder>(), Filterable {

    private var listSeller = ArrayList<SellerEntity>()
    private var listSellerFilter = ArrayList<SellerEntity>()

    fun setListSellerItem(listSellerItem: List<SellerEntity>?) {
        if (listSellerItem == null) return
        this.listSeller.clear()
        this.listSeller.addAll(listSellerItem)
        this.listSellerFilter = listSellerItem as ArrayList<SellerEntity>
        notifyDataSetChanged()
    }

    class SellerItemHolder(private val binding: ItemListSellerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listSellerItem: SellerEntity) {
            with(binding) {
                tvSellerName.text = listSellerItem.nama_penjual
                tvSellerShop.text = listSellerItem.nama_toko
                tvRatingNumber.text = listSellerItem.rating
                Glide.with(itemView.context)
                    .load(listSellerItem.foto_path)
                    .transform(CenterCrop())
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_profile)
                    .into(imgSeller)

                if (listSellerItem.status == "blocked") {
                    visibility.visibility = View.VISIBLE
                }

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, SellerDetailActivity::class.java)
                        .apply {
                            putExtra(SellerDetailActivity.idpenjual, listSellerItem.idpenjual)
                            putExtra(SellerDetailActivity.nama_penjual, listSellerItem.nama_penjual)
                            putExtra(SellerDetailActivity.nama_toko, listSellerItem.nama_toko)
                            putExtra(SellerDetailActivity.alamat_penjual, listSellerItem.alamat_penjual)
                            putExtra(SellerDetailActivity.email_penjual, listSellerItem.email_penjual)
                            putExtra(SellerDetailActivity.nohp_penjual, listSellerItem.nohp_penjual)
                            putExtra(SellerDetailActivity.statusPenjual, listSellerItem.status)
                            putExtra(SellerDetailActivity.rating, listSellerItem.rating)
                            putExtra(SellerDetailActivity.foto_path, listSellerItem.foto_path)
                        }
                    itemView.context.startActivity(intent)
                    (itemView.context as Activity).finish()
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellerItemHolder {
        val itemProductBinding = ItemListSellerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SellerItemHolder(itemProductBinding)
    }

    override fun onBindViewHolder(holder: SellerItemHolder, position: Int) {
        val userItem = listSeller[position]
        holder.bind(userItem)
    }

    override fun getItemCount(): Int {
        return listSeller.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                val filterResults = FilterResults()

                if (constraint == null || constraint.length < 0) {
                    filterResults.count = listSellerFilter.size
                    filterResults.values = listSellerFilter
                } else {
                    val charSearch = constraint.toString()

                    val resultList = ArrayList<SellerEntity>()

                    for (row in listSellerFilter) {
                        if (row.nama_penjual.toLowerCase(Locale.getDefault()).contains(charSearch.toLowerCase(Locale.getDefault()))) {
                            resultList.add(row)
                        }
                    }
                    filterResults.count = resultList.size
                    filterResults.values = resultList
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                listSeller = results?.values as ArrayList<SellerEntity>
                notifyDataSetChanged()
            }

        }
    }
}



