package com.example.aiexpenzo.data.model

data class Expense(
    val description: String,
    val category: String,
    val paymentMethod: String,
    val amount: Double
)