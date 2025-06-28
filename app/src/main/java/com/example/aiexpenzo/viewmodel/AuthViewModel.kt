package com.example.aiexpenzo.viewmodel

import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.compose.animation.core.snap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aiexpenzo.data.firebase.FirebaseService
import com.example.aiexpenzo.data.model.User
import com.example.aiexpenzo.data.repository.FirestoreUserRepository
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Calendar

class AuthViewModel:ViewModel() {

    private val auth = FirebaseService.auth
    private val db = FirebaseService.firestore

    // private mutable state flow that holds authentication status (default = false)
    private val _authSuccess = MutableStateFlow(false)

    // public read-only state flow exposed to UI layer to observe authentication success
    val authSuccess: StateFlow<Boolean> = _authSuccess.asStateFlow()

    // private mutablle state flow that holds error messages (default = null)
    private val _errorMessage = MutableStateFlow<String?>(null)

    // public read-only state flow exposed to UI layer to observe errors
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // COMMENT
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _profileUpdateSuccess = MutableStateFlow(false)
    val profileUpdateSuccess: StateFlow<Boolean> = _profileUpdateSuccess.asStateFlow()

    private val _passwordVerificationNeeded = MutableStateFlow(false)
    val passwordVerificationNeeded: StateFlow<Boolean> = _passwordVerificationNeeded.asStateFlow()

    private val _currentPassword = MutableStateFlow("")
    val currentPassword: StateFlow<String> = _currentPassword.asStateFlow()

    // Loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        auth.addAuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser != null){
                setupUserlistener()
            } else {
                _currentUser.value = null
            }
        }
    }

    private fun setupUserlistener(){
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid)
            .addSnapshotListener{ snapshot, error ->
                if (error != null) return@addSnapshotListener
                _currentUser.value = snapshot?.toObject(User::class.java)
            }

    }

    // Function: Sign Up - handles user registration
    fun signUp(name: String, email:String, password: String){
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val calendar = Calendar.getInstance()
                val month = calendar.get(Calendar.MONTH)
                val year = calendar.get(Calendar.YEAR)
                val user = User(
                    uid = result.user?.uid?: "",
                    name = name,
                    email = email,
                    monthlyBudget = emptyMap(),
                    monthlyIncome = emptyMap()
                )
                FirestoreUserRepository.saveUser(user)
                _currentUser.value = user
                _authSuccess.value = true
            } catch (e: Exception){
                _errorMessage.value = e.message ?: "Sign up failed"
                _authSuccess.value = false

            } finally {
                _isLoading.value = false
            }
        }
    }

    // Function: Login
    fun login(email: String, password: String){
        viewModelScope.launch{
            _isLoading.value = true
            try{
                auth.signInWithEmailAndPassword(email, password).await()
                val user = FirestoreUserRepository.getUser()
                _currentUser.value = user
                _authSuccess.value = true
                _isLoading.value = false
            } catch (e: Exception){
                _errorMessage.value = e.message ?: "Login failed"
                _authSuccess.value = false
                _isLoading.value = false
            } finally {
                _isLoading.value = false
            }
        }


    }

    // Function - log users out
    fun logout(){
       auth.signOut()
        _currentUser.value = null
        _authSuccess.value = false

    }

    // Function: Set Monthly Income
    fun setMonthlyIncome(month: Int, year: Int, income: Float){
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val monthYearKey = "$month-$year"
                FirestoreUserRepository.setMonthlyIncome(month, year, income)

                val updatedUser = FirestoreUserRepository.getUser()
                _currentUser.value = updatedUser

                _currentUser.value = _currentUser.value?.copy(
                    monthlyIncome = _currentUser.value?.monthlyIncome.orEmpty().toMutableMap().apply{
                        this[monthYearKey] = income
                    }

                )

            } finally {
                _isLoading.value = false
            }

        }
    }

    fun getCurrentMonthIncome(month: Int, year: Int): Float {
        val monthYearKey = "$month-$year"
        return _currentUser.value?.monthlyIncome?.get(monthYearKey) ?: 0f
    }

    // Function: Set Monthly Budget
    fun setMonthlyBudget(month: Int, year: Int, budget: Float){
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val monthYearKey = "$month-$year"
                FirestoreUserRepository.setMonthlyBudget(month, year, budget)

                val updatedUser = FirestoreUserRepository.getUser()
                _currentUser.value = updatedUser

                _currentUser.value = _currentUser.value?.copy(
                    monthlyBudget = _currentUser.value?.monthlyBudget.orEmpty().toMutableMap().apply {
                        this[monthYearKey] = budget
                    }
                )
            } finally {
                _isLoading.value = false

            }

        }
    }

    fun getCurrentMonthBudget(month: Int, year: Int): Float {
        val monthYearKey = "$month-$year"
        return _currentUser.value?.monthlyBudget?.get(monthYearKey) ?: 0f
    }


    // Function - validate value of monthly budget (should not be more than income)
    fun isBudgetValid(month: Int, year: Int, budget: Float): Boolean{
        val income = getCurrentMonthIncome(month, year)
        return budget <= income
    }

    suspend fun updateEmailWithReauth(newEmail: String, currentPassword: String): Boolean {
        val user = auth.currentUser ?: return false

        return try {
            // Reauthenticate
            val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)
            auth.signInWithCredential(credential).await()

            //  Verify if email needs changing
            if (newEmail == user.email){
                return true
            }
            // Update email
            user.updateEmail(newEmail).await()

            // Update Firestore
            db.collection("users").document(user.uid)
                .update("email", newEmail).await()

            // Update local state
            _currentUser.value = _currentUser.value?.copy(email = newEmail)
            true
        } catch (e: Exception) {
            Log.e("EMAIL_UPDATE", "Error: ${e.javaClass.simpleName}", e)
            _errorMessage.value = when (e) {
                is com.google.firebase.auth.FirebaseAuthInvalidCredentialsException ->
                    "Invalid current password"
                is com.google.firebase.auth.FirebaseAuthUserCollisionException ->
                    "Email already in use"
                else -> "Failed to update email: ${e.message}"
            }
            false
        }
    }

    // To update Profile in Edit Profile Screen
    fun updateProfile(
        newName: String,
        newEmail: String,
        newPassword: String,
        currentPassword: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val user = auth.currentUser ?: run{
                    onError("User not authenticated")
                    return@launch
                }

                // Update email if changed
                if (newEmail != user.email) {
                    if (!updateEmailWithReauth(newEmail, currentPassword))
                        return@launch
                }
                // Update password if changed
                if (newPassword.isNotEmpty()) {
                    try {
                        user.updatePassword(newPassword).await()
                    } catch (e: Exception){
                        onError("Password update failed: ${e.message ?: "Unknown error"}")
                    }

                }

                // Update name in Firestore
                try {
                    FirestoreUserRepository.updateUserName(newName)
                    _currentUser.value = _currentUser.value?.copy(name = newName)
                } catch (e: Exception){
                    onError("Name update failed: ${e.message ?: "Unknown error"}")
                    return@launch
                }

                // Refresh user data
                _currentUser.value = FirestoreUserRepository.getUser()
                _profileUpdateSuccess.value = true
                onSuccess()
            } catch (e: Exception) {
                when (e) {
                    is com.google.firebase.auth.FirebaseAuthInvalidCredentialsException ->
                        onError("Invalid current password")
                    is com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException ->
                        onError("Recent login required - please reauthenticate")
                    else ->
                        onError("Profile update failed: ${e.message ?: "Unknown error"}")
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    //Helpers

    // Function - resets authentication status to false
    // for navigating away from auth screens/retrying auth
    fun resetAuthStatus(){
        _authSuccess.value = false
    }

    // Function - clears any existing error messages
    // for when user starts typing again/retrying auth
    fun clearError(){
        _errorMessage.value = null
    }


}