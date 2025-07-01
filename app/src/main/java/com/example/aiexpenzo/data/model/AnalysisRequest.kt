package com.example.aiexpenzo.data.model

import com.google.gson.annotations.SerializedName

data class AnalysisRequest(
    val expenses: List<ExpenseBody>,
    @SerializedName("time_range")
    val timeRange: String = "Overall"
)