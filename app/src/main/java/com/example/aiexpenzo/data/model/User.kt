package com.example.aiexpenzo.data.model

// Special class to hold user data
data class User(
    val name: String,
    val email: String,
    val password: String,
    val monthlyIncome: Double = 0.0,
    val monthlyBudget: Double = 0.0
)