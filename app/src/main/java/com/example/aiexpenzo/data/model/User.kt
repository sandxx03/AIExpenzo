package com.example.aiexpenzo.data.model

// Full user data for Firestore
data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val monthlyIncome: Map<String, Float> = emptyMap(), // Key format: "MM-YYYY"
    val monthlyBudget: Map<String, Float> = emptyMap()
)