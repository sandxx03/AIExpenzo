package com.example.aiexpenzo.data.model

import java.io.Serializable

data class Expense(
    val id: Long = System.currentTimeMillis(),  //generate unique ID
    val description: String = "",
    val category: String = "",
    val paymentMethod: String = "",
    val amount: Double = 0.0,
    val dateMillis: Long = 0L
) : Serializable