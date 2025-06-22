package com.example.aiexpenzo.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.aiexpenzo.data.model.CategorySpend
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

    // Function - get Expense Data for Chart
    fun getDailyTotalsForMonth(month: Int, year: Int): List<Float>{
        val daysInMonth = Calendar.getInstance().apply {
            set(Calendar.MONTH, month)
            set(Calendar.YEAR, year)
        }.getActualMaximum(Calendar.DAY_OF_MONTH)

        val totals = MutableList(daysInMonth){ 0f }

        _allExpenses.forEach { expense ->
            val cal = Calendar.getInstance().apply{timeInMillis = expense.dateMillis}
            if (cal.get(Calendar.MONTH) == month && cal.get(Calendar.YEAR) == year){
                val day = cal.get(Calendar.DAY_OF_MONTH) -1
                totals[day] = totals[day] + expense.amount.toFloat()
            }

        }
        return totals
    }

    // Function - get user's monthly total expenses for Dashboard
    fun getTotalExpensesForMonth(month: Int, year:Int):Float{
        return _allExpenses
            .filter {
                val cal = Calendar.getInstance()
                cal.timeInMillis = it.dateMillis
                cal.get(Calendar.MONTH) == month && cal.get(Calendar.YEAR) == year
            }
            .sumOf{ it.amount.toDouble()}.toFloat()
    }

    // Function - get top 3 categories spent by user
    fun getTopCategories(month: Int, year: Int): List<CategorySpend>{
                val filtered = _allExpenses.filter {
                val cal = Calendar.getInstance()
                cal.timeInMillis = it.dateMillis
                cal.get(Calendar.MONTH) == month && cal.get(Calendar.YEAR) == year
            }

            return filtered.groupBy { it.category }
                .map { (cat, items) ->
                    val latestDate = items.maxByOrNull { it.dateMillis }?.dateMillis ?: 0L
                    val formattedDate = SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(Date(latestDate))
                    CategorySpend(cat, items.sumOf { it.amount }.toFloat(), formattedDate)
                }
                .sortedByDescending { it.amount }
                .take(3)
    }
}