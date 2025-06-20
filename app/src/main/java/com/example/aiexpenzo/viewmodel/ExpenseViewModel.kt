package com.example.aiexpenzo.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.aiexpenzo.data.model.Expense
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ExpenseViewModel: ViewModel() {
    private val _allExpenses = mutableStateListOf<Expense>()

    fun addExpense(expense: Expense){
        _allExpenses.add(expense)
    }

    fun getExpensesForMonth(month: Int, year:Int): Map<String, List<Expense>>{
        val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

        return _allExpenses
            .filter {
                val cal = Calendar.getInstance()
                cal.timeInMillis = it.dateMillis
                cal.get(Calendar.MONTH) == month && cal.get(Calendar.YEAR) == year
            }
            .sortedByDescending { it.dateMillis }
            .groupBy { expense ->
                formatter.format(Date(expense.dateMillis))
            }

    }

    fun hasExpensesForMonth(month: Int, year: Int): Boolean{
        return getExpensesForMonth(month, year).isNotEmpty()
    }

    // Function - update Expense Item when edited
    fun updateExpense(updatedExpense:Expense){
        val index = _allExpenses.indexOfFirst { it.id == updatedExpense.id }
        if (index != -1){
            _allExpenses[index] = updatedExpense
        }
    }

    // Function - delete Expense Item
    fun removeExpense(expense: Expense){
        _allExpenses.removeIf{it.id == expense.id}
    }
}