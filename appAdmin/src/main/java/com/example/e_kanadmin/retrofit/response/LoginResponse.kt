package com.example.e_kanadmin.retrofit.response

import com.example.e_kanadmin.auth.AdminEntity

class LoginResponse(
    var status: String = "",
    var data: ArrayList<AdminEntity>,
    var tokenAuth: String = ""
)

