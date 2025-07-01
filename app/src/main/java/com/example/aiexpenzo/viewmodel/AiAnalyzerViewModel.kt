package com.example.aiexpenzo.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aiexpenzo.data.model.Expense
import com.example.aiexpenzo.data.repository.AiAnalyzerRepository
import kotlinx.coroutines.launch

class AiAnalyzerViewModel : ViewModel(){
    private val repo = AiAnalyzerRepository()

    private val _result = mutableStateOf<List<String>?>(null)
    val result: State<List<String>?> = _result

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    fun analyze(expenses: List<Expense>, timeRange: String = "Overall"){
        viewModelScope.launch {
            _isLoading.value = true
            _result.value = try{
                repo.analyze(expenses, timeRange)
            } catch (e: Exception){
                listOf("Error: ${e.message}")
            }
            _isLoading.value = false
        }
    }
}