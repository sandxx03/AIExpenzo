package com.example.aiexpenzo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aiexpenzo.data.constants.EXPENSE_CATEGORIES
import com.example.aiexpenzo.data.firebase.FirebaseService.auth
import com.example.aiexpenzo.data.model.CategorySpend
import com.example.aiexpenzo.data.model.Expense
import com.example.aiexpenzo.data.repository.FirestoreExpenseRepository
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ExpenseViewModel: ViewModel() {
    private val repo = FirestoreExpenseRepository
    private val _allExpenses = MutableStateFlow<List<Expense>>(emptyList())
    val allExpense: StateFlow<List<Expense>> = _allExpenses.asStateFlow()

    // Loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var expensesListener: ListenerRegistration? = null

    init {
        auth.addAuthStateListener { authState ->
            if(authState.currentUser == null){
                clearData()
            } else{
                setupExpenseListener()
            }
        }
    }

    private fun setupExpenseListener(){
        _isLoading.value = true
        repo.listenToExpenses(
            onDataChanged = { expenses ->
                _allExpenses.value = expenses
                _isLoading.value = false
            },
            onError = {
                _isLoading.value = false
            }
        )

    }

    override fun onCleared() {
        expensesListener?.remove()
        super.onCleared()
    }

    fun addExpense(expense: Expense){
        viewModelScope.launch {
            _isLoading.value = true

            try {
                // Firestore listener will sync and update
                repo.addExpense(expense)
            } finally {
                _isLoading.value = false

            }

        }
    }

    // Function - update Expense Item when edited
    fun updateExpense(updatedExpense:Expense){
        viewModelScope.launch {
            _isLoading.value = true
            try{
                repo.updateExpense(updatedExpense)
            }finally {
                _isLoading.value = false
            }
        }
    }

    // Function - delete Expense Item
    fun removeExpense(expense: Expense){
       viewModelScope.launch {
           _isLoading.value = true
           try {
               repo.deleteExpense(expense.id)
           } finally {
               _isLoading.value = false
           }
       }
    }

    fun getExpensesForMonth(month: Int, year:Int): Map<String, List<Expense>>{
        val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

        return _allExpenses.value
            .filter { expense ->
                val cal = Calendar.getInstance().apply { time = expense.transactionDate }
                cal.get(Calendar.MONTH) == month && cal.get(Calendar.YEAR) == year
            }
            .sortedByDescending { it.transactionDate.time }
            .groupBy { expense ->
                formatter.format(expense.transactionDate)
            }
    }

    fun hasExpensesForMonth(month: Int, year: Int): Boolean{
        return getExpensesForMonth(month, year).isNotEmpty()
    }

    // Function - get Expense Data for Line Chart
    fun getDailyTotalsForMonth(month: Int, year: Int): List<Float>{
        val daysInMonth = Calendar.getInstance().apply {
            set(Calendar.MONTH, month)
            set(Calendar.YEAR, year)
        }.getActualMaximum(Calendar.DAY_OF_MONTH)

        val totals = MutableList(daysInMonth){ 0f }

        _allExpenses.value.forEach { expense ->
            val cal = Calendar.getInstance().apply{time = expense.transactionDate}
            if (cal.get(Calendar.MONTH) == month && cal.get(Calendar.YEAR) == year){
                val day = cal.get(Calendar.DAY_OF_MONTH) -1
                totals[day] = totals[day] + expense.amount.toFloat()
            }
        }
        return totals
    }

    // Function - get user's monthly total expenses for Dashboard
    fun getTotalExpensesForMonth(month: Int, year:Int):Float{
        return _allExpenses.value
            .filter { expense ->
                val cal = Calendar.getInstance().apply {
                    time = expense.transactionDate
                }
                cal.get(Calendar.MONTH) == month && cal.get(Calendar.YEAR) == year
            }
            .sumOf{ it.amount.toDouble()}.toFloat()
    }

    // Function - get top 3 categories spent by user
    fun getTopCategories(month: Int, year: Int): List<CategorySpend>{
                val filtered = _allExpenses.value
                    .filter { expense ->
                val cal = Calendar.getInstance().apply {
                    time = expense.transactionDate
                }
                cal.get(Calendar.MONTH) == month && cal.get(Calendar.YEAR) == year
            }
            return filtered.groupBy { it.category }
                .map { (cat, items) ->
                    val latestDate = items.maxByOrNull { it.transactionDate.time }?.transactionDate ?: 0L
                    val formattedDate = SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(latestDate)
                    CategorySpend(cat, items.sumOf { it.amount }.toFloat(), formattedDate)
                }
                .sortedByDescending { it.amount }
                .take(3)
    }

    // Function - get total spent per category (all categories) for Bar Chart
    fun getAllCategories(month: Int, year: Int): List<CategorySpend>{
        val expensesThisMonth = _allExpenses.value.filter { expense ->
            val cal = Calendar.getInstance().apply { time = expense.transactionDate }
            cal.get(Calendar.MONTH) == month && cal.get(Calendar.YEAR) == year
        }
        val groupedExpenses = expensesThisMonth.groupBy { it.category }
        return EXPENSE_CATEGORIES.map { categoryName ->
            val expensesInCategory = groupedExpenses[categoryName] ?: emptyList()

            val totalAmount = expensesInCategory.sumOf { it.amount }.toFloat()

            val latestDate = expensesInCategory.maxByOrNull { it.transactionDate.time }?.transactionDate
            val formattedDate = if (latestDate != null) {
                SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(latestDate)
            } else {
                "-"
            }
            CategorySpend(
                title = categoryName,
                amount = totalAmount,
                date = formattedDate
            )
        }.sortedByDescending { it.amount }
    }

    fun clearData(){
        repo.removeListener()
        _allExpenses.value = emptyList()
        _isLoading.value = false
    }

}