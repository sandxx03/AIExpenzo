package com.example.aiexpenzo.data.model

// Special class to hold user data
data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val monthlyIncome: Map<String, Float> = emptyMap(), // Key format: "MM-YYYY"
    val monthlyBudget: Map<String, Float> = emptyMap()
)