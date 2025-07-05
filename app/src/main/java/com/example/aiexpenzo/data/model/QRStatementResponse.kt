package com.example.aiexpenzo.data.model

data class QRStatementResponse(
    val amount: Double?,
    val payment_method: String?,
    val description: String?,
    val transaction_date: String?
)