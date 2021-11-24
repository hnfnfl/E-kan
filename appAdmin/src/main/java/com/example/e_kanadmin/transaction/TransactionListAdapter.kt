package com.example.e_kanadmin.transaction

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.e_kanadmin.R
import com.example.e_kanadmin.databinding.ItemTransactionListBinding


class TransactionListAdapter : RecyclerView.Adapter<TransactionListAdapter.TransactionItemHolder>() {

    private var transactionList = ArrayList<TransactionEntity>()

    fun setListTransactionItem(listTransactionItem: List<TransactionEntity>?) {
        if (listTransactionItem == null) return
        this.transactionList.clear()
        this.transactionList.addAll(listTransactionItem)
        notifyDataSetChanged()
    }

    class TransactionItemHolder(private val binding: ItemTransactionListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(transactionItem: TransactionEntity) {
            with(binding) {
                tvUserName.text = transactionItem.nama
                tvOrderId.text = transactionItem.kode_pesanan
                tvOrderQty.text = transactionItem.pesanan.size.toString()
                val statusTransaksi = transactionItem.transaksi
                tvStatus.text = statusTransaksi
                when (statusTransaksi) {
                    "tertunda" -> {
                        tvStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.md_deep_orange_400))
                    }
                    "disetujui" -> {
                        tvStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.md_green_400))
                    }
                    "ditolak" -> {
                        tvStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.md_red_600))
                    }
                    "dibatalkan" -> {
                        tvStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.md_blue_grey_400))
                    }
                }
                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, TransactionDetailActivity::class.java)
                        .apply {
                            putExtra(TransactionDetailActivity.idpesanan, transactionItem.idpesanan)
                            putExtra(TransactionDetailActivity.nama, transactionItem.nama)
                            putExtra(TransactionDetailActivity.namaToko, transactionItem.nama_toko)
                            putExtra(TransactionDetailActivity.kodePesanan, transactionItem.kode_pesanan)
                            putExtra(TransactionDetailActivity.tglPesan, transactionItem.tglPesan)
                            putExtra(TransactionDetailActivity.tglTransfer, transactionItem.tglTransfer)
                            putExtra(TransactionDetailActivity.catatan, transactionItem.catatan)
                            putExtra(TransactionDetailActivity.alamat, transactionItem.alamat_antar)
                            putExtra(TransactionDetailActivity.fotoPath, transactionItem.foto_path)
                            putExtra(TransactionDetailActivity.status, transactionItem.status)
                            putExtra(TransactionDetailActivity.transaksi, transactionItem.transaksi)
                        }
                    intent.putExtra("pesanan", transactionItem.pesanan)
                    itemView.context.startActivity(intent)
                    (itemView.context as Activity).finish()
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionItemHolder {
        val itemTransactionBinding = ItemTransactionListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionItemHolder(itemTransactionBinding)
    }

    override fun onBindViewHolder(holder: TransactionItemHolder, position: Int) {
        val transactionItem = transactionList[position]
        holder.bind(transactionItem)
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

}



