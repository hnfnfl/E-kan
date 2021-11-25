package com.example.e_kanadmin.listUser

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_kanadmin.R
import com.example.e_kanadmin.databinding.ItemListUserBinding
import java.util.*
import kotlin.collections.ArrayList

class ListUserAdapter : RecyclerView.Adapter<ListUserAdapter.UserItemHolder>(), Filterable {

    private var listUser = ArrayList<UserEntity>()
    private var listUserFilter = ArrayList<UserEntity>()

    fun setListUserItem(listUserItem: List<UserEntity>?) {
        if (listUserItem == null) return
        this.listUser.clear()
        this.listUser.addAll(listUserItem)
        this.listUserFilter = listUserItem as ArrayList<UserEntity>
        notifyDataSetChanged()
    }

    class UserItemHolder(private val binding: ItemListUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listUserItem: UserEntity) {
            with(binding) {
                tvUserName.text = listUserItem.nama
                tvUserEmail.text = listUserItem.email
                Glide.with(itemView.context)
                    .load(listUserItem.foto_path)
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_profile)
                    .into(imgUser)
                itemView.setOnClickListener {

                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserItemHolder {
        val itemProductBinding = ItemListUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserItemHolder(itemProductBinding)
    }

    override fun onBindViewHolder(holder: UserItemHolder, position: Int) {
        val userItem = listUser[position]
        holder.bind(userItem)
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                val filterResults = FilterResults()

                if (constraint == null || constraint.length < 0) {
                    filterResults.count = listUserFilter.size
                    filterResults.values = listUserFilter
                } else {
                    val charSearch = constraint.toString()

                    val resultList = ArrayList<UserEntity>()

                    for (row in listUserFilter) {
                        if (row.nama.toLowerCase(Locale.getDefault()).contains(charSearch.toLowerCase(Locale.getDefault()))) {
                            resultList.add(row)
                        }
                    }
                    filterResults.count = resultList.size
                    filterResults.values = resultList
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                listUser = results?.values as ArrayList<UserEntity>
                notifyDataSetChanged()
            }

        }
    }
}



