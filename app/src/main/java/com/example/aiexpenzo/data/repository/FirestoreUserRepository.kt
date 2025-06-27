package com.example.aiexpenzo.data.repository

import com.example.aiexpenzo.data.firebase.FirebaseService
import com.example.aiexpenzo.data.model.User
import kotlinx.coroutines.tasks.await

object FirestoreUserRepository{

    private val db = FirebaseService.firestore
    private val auth = FirebaseService.auth

    suspend fun saveUser(user: User){
        val uid = auth.currentUser?.uid ?: return
        val userToSave = user.copy(
            monthlyIncome = user.monthlyIncome ?: emptyMap(),
            monthlyBudget = user.monthlyBudget ?: emptyMap())
        db.collection("users").document(uid).set(userToSave).await()

    }

    suspend fun getUser(): User?{
        val uid = auth.currentUser?.uid ?: return null
        val snapshot = db.collection("users").document(uid).get().await()
        return snapshot.toObject(User::class.java)
    }

    suspend fun updateUser(updated: User){
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid).set(updated).await()
    }

    suspend fun setMonthlyIncome(month: Int, year: Int, amount: Float){
        val uid = auth.currentUser?.uid ?: return
        val monthYearKey = "$month-$year"
        db.collection("users").document(uid)
            .update("monthlyIncome.$monthYearKey", amount)
            .await()
    }

    suspend fun setMonthlyBudget(month: Int, year: Int, amount: Float){
        val uid = auth.currentUser?.uid ?: return
        val monthYearKey = "$month-$year"
        db.collection("users").document(uid)
            .update("monthlyBudget.$monthYearKey", amount)
            .await()
    }

}