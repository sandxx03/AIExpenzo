package com.example.aiexpenzo.components

enum class BottomNavBarItem(
    val label: String,
    val route: String
) {
    Home("Home", "dashboard"),
    Expenses("Expenses", "expense_list"),
    Analyzer("AI Analyzer", "ai_analyzer"),
    Profile("Profile", "profile")
}