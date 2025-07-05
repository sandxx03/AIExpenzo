package com.example.aiexpenzo.data.remote

import com.example.aiexpenzo.data.model.QRStatementRequest
import com.example.aiexpenzo.data.model.QRStatementResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface QRStatementApi {
    @POST("parse_statement")
    suspend fun parseStatement(@Body request: QRStatementRequest): QRStatementResponse
}