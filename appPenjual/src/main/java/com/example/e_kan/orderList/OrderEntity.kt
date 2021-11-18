package com.example.e_kan.orderList

class OrderEntity(
    var idpesanan: String = "",
    var nama: String = "",
    var pesanan: ArrayList<OrderItemEntity>,
    var kode_pesanan: String = "",
    var alamat_antar: String = "",
    var catatan: String = "",
    var timestamp: String = "",
    var status: String = "",
)