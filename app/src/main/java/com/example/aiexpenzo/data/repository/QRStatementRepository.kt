package com.example.aiexpenzo.data.repository

import com.example.aiexpenzo.data.model.QRStatementRequest
import com.example.aiexpenzo.data.model.QRStatementResponse
import com.example.aiexpenzo.data.remote.ApiClient
import com.example.aiexpenzo.data.remote.QRStatementApi

class QRStatementRepository {
    private val api = ApiClient.retrofit.create(QRStatementApi::class.java)

    suspend fun parseStatement(statement: String): QRStatementResponse{
        return api.parseStatement(QRStatementRequest(statement))
    }
}