package com.example.aiexpenzo.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aiexpenzo.data.firebase.FirebaseService
import com.example.aiexpenzo.data.model.User
import com.example.aiexpenzo.data.repository.FirestoreUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Calendar

class AuthViewModel:ViewModel() {
    private val auth = FirebaseService.auth

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

    // Loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Function: Sign Up - handles user registration
    fun signUp(name: String, email:String, password: String){
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val calendar = Calendar.getInstance()
                val month = calendar.get(Calendar.MONTH)
                val year = calendar.get(Calendar.YEAR)
                val monthYearKey = "$month-$year"
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
            }
        }


    }

    // Function - log users out
    fun logout(){
       auth.signOut()
        _currentUser.value = null
    }

    // Function: Set Monthly Income
    fun setMonthlyIncome(month: Int, year: Int, income: Float){
        viewModelScope.launch {
            val uid = auth.currentUser?.uid ?: return@launch
            val monthYearKey = "$month-$year"
            FirestoreUserRepository.setMonthlyIncome(month, year, income)
            _currentUser.value = _currentUser.value?.copy(
                monthlyIncome = _currentUser.value?.monthlyIncome.orEmpty() + (monthYearKey to income)

            )
        }
    }

    fun getCurrentMonthIncome(month: Int, year: Int): Float {
        val monthYearKey = "$month-$year"
        return _currentUser.value?.monthlyIncome?.get(monthYearKey) ?: 0f
    }

    // Function: Set Monthly Budget
    fun setMonthlyBudget(month: Int, year: Int, budget: Float){
        viewModelScope.launch {
            val uid = auth.currentUser?.uid ?: return@launch
            val monthYearKey = "$month-$year"
            FirestoreUserRepository.setMonthlyBudget(month, year, budget)
            _currentUser.value = _currentUser.value?.copy(
                monthlyBudget = _currentUser.value?.monthlyBudget.orEmpty() + (monthYearKey to budget)

            )
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


    fun validateProfileInput(name: String, email: String, password: String): String? {
        return when {
            name.isBlank() || email.isBlank() || password.isBlank() ->
                "Please fill in all the fields."
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                "Please enter a valid email address."
            password.length < 6 ->
                "Password must be at least 6 characters."
            else -> null // No errors
        }
    }

    // Function: Update Username
    fun updateUsername(newName: String){
        val currentUser = auth.currentUser
        currentUser?.updateEmail(newName)
            ?.addOnSuccessListener {  }
            ?.addOnFailureListener{
                _errorMessage.value = it.message
            }
    }

    // Function: Update Email
    fun updateEmail(newEmail: String){
        val currentUser = auth.currentUser
        currentUser?.updateEmail(newEmail)
            ?.addOnSuccessListener {  }
            ?.addOnFailureListener{
                _errorMessage.value = it.message
            }
    }

    // Function: Update Password
    fun updatePassword(newPassword: String){
        val currentUser = auth.currentUser
        currentUser?.updatePassword(newPassword)
            ?.addOnSuccessListener {  }
            ?.addOnFailureListener{
                _errorMessage.value = it.message
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