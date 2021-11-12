package com.example.e_kan.retrofit.response

import com.example.e_kan.auth.SellerEntity

class LoginResponse(
    var status: String = "",
    var data: ArrayList<SellerEntity>,
    var tokenAuth: String = ""
)

