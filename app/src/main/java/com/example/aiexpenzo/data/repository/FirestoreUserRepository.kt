package com.example.aiexpenzo.data.repository

import com.example.aiexpenzo.data.firebase.FirebaseService
import com.example.aiexpenzo.data.model.User
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await

// object: Singleton (only one instance)
// Centralized class to manage user data in Firestore
object FirestoreUserRepository{

    // get Firestore and Auth instances for database and authentication operations.
    private val db = FirebaseService.firestore
    private val auth = FirebaseService.auth
    private var userListener: ListenerRegistration? = null

    fun listenToUser(
        onUserChanged:(User?) -> Unit
    ){
        removeUserListener()
        val uid = FirebaseService.auth.currentUser?.uid ?: return
        userListener = FirebaseService.firestore
            .collection("users")
            .document(uid)
            .addSnapshotListener { snapshot, _ ->
                onUserChanged(snapshot?.toObject(User::class.java))
            }
    }

    fun removeUserListener(){
        userListener?.remove()
    }

    // suspend: runs function asynchronously (doesn't block UI)
    suspend fun saveUser(user: User){
        val uid = auth.currentUser?.uid ?: return   // get current user's UID if logged in, else return null if no current user
        val userToSave = user.copy(
            monthlyIncome = user.monthlyIncome ,
            monthlyBudget = user.monthlyBudget)
        // saves user object to Firestore under /users/uid
        //.await() suspends until Firestore finishes the save operation
        db.collection("users").document(uid).set(userToSave).await()

    }

    suspend fun getUser(): User?{
        val uid = auth.currentUser?.uid ?: return null
        val snapshot = db.collection("users").document(uid).get().await()
        return snapshot.toObject(User::class.java)  // converts fetched document back into user data model structure
    }

    suspend fun updateUser(updated: User){
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid).set(updated).await()
    }

    suspend fun setMonthlyIncome(month: Int, year: Int, amount: Float){
        val uid = auth.currentUser?.uid ?: return   // get UID of current user
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

    suspend fun updateUserName(name: String) {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid)
            .update("name", name)
            .await()
    }

}