package com.example.e_kanadmin.transaction

class TransactionEntity(
    var idpesanan: String = "",
    var nama: String = "",
    var nama_toko: String = "",
    var pesanan: ArrayList<OrderItemEntity>,
    var kode_pesanan: String = "",
    var alamat_antar: String = "",
    var catatan: String = "",
    var foto_path: String = "",
    var tglTransfer: String = "",
    var tglPesan: String = "",
    var status: String = "",
    var transaksi: String = "",

    )