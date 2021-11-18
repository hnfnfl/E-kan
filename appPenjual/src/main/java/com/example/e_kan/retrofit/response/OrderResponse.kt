package com.example.e_kan.retrofit.response

import com.example.e_kan.orderList.OrderEntity

class OrderResponse(
    var status: String = "",
    var data: ArrayList<OrderEntity>
)