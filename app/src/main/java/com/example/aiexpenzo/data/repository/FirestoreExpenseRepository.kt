package com.example.aiexpenzo.data.repository

import com.example.aiexpenzo.data.firebase.FirebaseService
import com.example.aiexpenzo.data.model.Expense
import com.google.firebase.Timestamp
import kotlinx.coroutines.tasks.await

object FirestoreExpenseRepository {

    private val db = FirebaseService.firestore
    private val auth = FirebaseService.auth

    suspend fun addExpense(expense: Expense): Boolean {
        return try{
            val uid = auth.currentUser?.uid ?: return false
            val expenseData = hashMapOf(
                "id" to expense.id,
                "description" to expense.description,
                "category" to expense.category,
                "paymentMethod" to expense.paymentMethod,
                "amount" to expense.amount,
                "transactionDate" to Timestamp(expense.transactionDate),

            )
            db.collection("users").document(uid)
                .collection("expenses")
                .document(expense.id.toString())
                .set(expense)
                .await()
            true
        } catch (e: Exception){
            println("Error adding expense: ${e.message}")
            false
        }

    }

    suspend fun getAllExpenses(): List<Expense>{
        try{
            val uid = auth.currentUser?.uid ?: return emptyList()
            val snapshot = db.collection("users").document(uid)
                .collection("expenses")
                .get()
                .await()
            return snapshot.toObjects(Expense::class.java)
        } catch (e: Exception){
            println("Error fetching expenses: ${e.message}")
            return emptyList()
        }

    }

    suspend fun updateExpense(expense: Expense): Boolean {
        return try{
            val uid = auth.currentUser?.uid ?: return false
            db.collection("users").document(uid)
                .collection("expenses")
                .document(expense.id.toString())
                .set(expense)
                .await()
            true
        } catch (e: Exception){
            println("Error updating expense: ${e.message}")
            false
        }

    }

    suspend fun deleteExpense(expenseId: Long){
        try{
            val uid = auth.currentUser?.uid ?: return
            db.collection("users").document(uid)
                .collection("expenses").document(expenseId.toString()).delete().await()
        } catch (e: Exception){
            println("Error deleting expense: ${e.message}")
        }

    }
}