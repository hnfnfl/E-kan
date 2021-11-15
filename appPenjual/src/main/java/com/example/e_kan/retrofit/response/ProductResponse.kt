package com.example.e_kan.retrofit.response

import com.example.e_kan.product.ProductEntity

class ProductResponse(
    var status: String = "",
    var data: ArrayList<ProductEntity>
)