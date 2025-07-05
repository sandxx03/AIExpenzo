package com.example.aiexpenzo.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aiexpenzo.data.model.QRStatementResponse
import com.example.aiexpenzo.data.repository.QRStatementRepository
import kotlinx.coroutines.launch

class QRStatementViewModel : ViewModel(){
    private val repo = QRStatementRepository()

    private val _parsedExpense = mutableStateOf<QRStatementResponse?>(null)
    var parsedExpense: State<QRStatementResponse?> = _parsedExpense

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    fun extract(statement: String){
        viewModelScope.launch {
            _isLoading.value = true
            _parsedExpense.value = try {
                repo.parseStatement(statement)
            } catch (e: Exception){
                null
            }
            _isLoading.value = false
        }
    }

    fun clearParsedData(){
        _parsedExpense.value = null
    }

}