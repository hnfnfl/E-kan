package com.example.e_kanadmin.retrofit.response

import com.example.e_kanadmin.transaction.TransactionEntity

class TransactionResponse(
    var status: String = "",
    var data: ArrayList<TransactionEntity>
)

