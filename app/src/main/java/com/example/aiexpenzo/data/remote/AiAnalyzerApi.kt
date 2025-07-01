package com.example.aiexpenzo.data.remote

import com.example.aiexpenzo.data.model.AnalysisRequest
import com.example.aiexpenzo.data.model.AnalysisResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AiAnalyzerApi{
    @POST("analyze")
    suspend fun analyze(@Body request: AnalysisRequest): AnalysisResponse
}