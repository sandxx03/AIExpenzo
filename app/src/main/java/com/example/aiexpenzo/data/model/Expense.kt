package com.example.aiexpenzo.data.model

import java.io.Serializable
import java.util.Calendar
import java.util.Date

data class Expense(
    val id: Long = System.currentTimeMillis(),  //generate unique ID
    val description: String = "",
    val category: String = "",
    val paymentMethod: String = "",
    val amount: Double = 0.0,
    val transactionDate: Date = Date()
) : Serializable