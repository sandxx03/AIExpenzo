package com.example.aiexpenzo.data.repository

import com.example.aiexpenzo.data.model.AnalysisRequest
import com.example.aiexpenzo.data.model.Expense
import com.example.aiexpenzo.data.model.ExpenseBody
import com.example.aiexpenzo.data.remote.AiAnalyzerApi
import com.example.aiexpenzo.data.remote.ApiClient

class AiAnalyzerRepository {
    private val api = ApiClient.retrofit.create(AiAnalyzerApi::class.java)

    suspend fun analyze(
        expenses: List<Expense>,
        timeRange: String
    ): List<String>{
        val body = AnalysisRequest(
            expenses = expenses.map { e ->
               ExpenseBody(
                   amount = e.amount,
                   category = e.category,
                   description = e.description
                )
            },
            timeRange = timeRange
        )
        return api.analyze(body).analysis
    }
}