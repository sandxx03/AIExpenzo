package com.example.aiexpenzo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aiexpenzo.data.firebase.FirebaseService.auth
import com.example.aiexpenzo.data.model.User
import com.example.aiexpenzo.data.repository.FirestoreUserRepository
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Calendar

// ViewModel class for managing authentication-related logic and state
class AuthViewModel:ViewModel() {

    private val repo = FirestoreUserRepository

    // private mutable state flow that tracks authentication status whether success or not
    private val _authSuccess = MutableStateFlow(false)
    // public read-only state flow to expose authentication status to UI
    val authSuccess: StateFlow<Boolean> = _authSuccess.asStateFlow()

    // private mutable state flow that tracks any error messages during auth processes
    private val _errorMessage = MutableStateFlow<String?>(null)
    // public read-only state flow to expose error messages to UI
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // private mutable state flow that holds currently logged-in user details
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    // private mutable state flow that flags if profile update succeeded
    private val _profileUpdateSuccess = MutableStateFlow(false)
    val profileUpdateSuccess: StateFlow<Boolean> = _profileUpdateSuccess.asStateFlow()

    // Determines if password verification is required before sensitive updates
    private val _passwordVerificationNeeded = MutableStateFlow(false)
    val passwordVerificationNeeded: StateFlow<Boolean> = _passwordVerificationNeeded.asStateFlow()

    // Stores current password (for sensitive operations like password change)
    private val _currentPassword = MutableStateFlow("")
    val currentPassword: StateFlow<String> = _currentPassword.asStateFlow()

    // Loading indicator flag
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        // Listen for Firebase authentication state changes
        auth.addAuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser != null){
                setupUserlistener() // fetch user data when logged in
            } else {
                _currentUser.value = null
            }
        }
    }

    // Real-time listener to update local user data when Firestore changes
    private fun setupUserlistener(){
        repo.listenToUser { user ->
            _currentUser.value = user
        }
    }

    // Registers a new FirebaseAuth user + saves their profile in Firestore
    fun signUp(name: String, email:String, password: String){
        viewModelScope.launch {
            _isLoading.value = true // tells UI to load the loading spinner
            try {
                // Calls Firebase Authentication to create new user account
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val calendar = Calendar.getInstance()   // Gets current month and year
                val month = calendar.get(Calendar.MONTH)
                val year = calendar.get(Calendar.YEAR)
                // Creates user object
                val user = User(
                    uid = result.user?.uid?: "",
                    name = name,
                    email = email,
                    monthlyBudget = emptyMap(), // empty because user hasn't set these
                    monthlyIncome = emptyMap()
                )
                repo.saveUser(user)  // saves user object to Firestore
                _currentUser.value = user   // set to current user so the UI knows who is logged in
                _authSuccess.value = true
            } catch (e: Exception){
                _errorMessage.value = e.message ?: "Sign up failed"
                _authSuccess.value = false

            } finally {
                _isLoading.value = false
            }
        }
    }

    // Logs in existing FirebaseAuth user + fetches their Firestore profile
    fun login(email: String, password: String){
        viewModelScope.launch{
            _isLoading.value = true
            try{
                auth.signInWithEmailAndPassword(email, password).await()
                val user = repo.getUser()
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

    // Logs user out (FirebaseAuth)
    fun logout(){
       auth.signOut()
        _currentUser.value = null
        _authSuccess.value = false

    }

    // Sets monthly income in Firestore and updates local state
    fun setMonthlyIncome(month: Int, year: Int, income: Float){
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val monthYearKey = "$month-$year"
                repo.setMonthlyIncome(month, year, income)

                val updatedUser = repo.getUser()
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

    // Sets monthly budget in Firestore and updates local state
    // Function: Set Monthly Budget
    fun setMonthlyBudget(month: Int, year: Int, budget: Float){
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val monthYearKey = "$month-$year"
                repo.setMonthlyBudget(month, year, budget)

                val updatedUser = repo.getUser()
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

    // Updates user's name, email, password (if changed)
    fun updateProfile(
        newName: String,
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
                    repo.updateUserName(newName)
                    _currentUser.value = _currentUser.value?.copy(name = newName)
                } catch (e: Exception){
                    onError("Name update failed: ${e.message ?: "Unknown error"}")
                    return@launch
                }

                // Refresh user data
                _currentUser.value = repo.getUser()
                _profileUpdateSuccess.value = true
                onSuccess()
            } catch (e: Exception) {
                when (e) {
                    is FirebaseAuthInvalidCredentialsException ->
                        onError("Invalid current password")
                    is FirebaseAuthRecentLoginRequiredException ->
                        onError("Recent login required - please reauthenticate")
                    else ->
                        onError("Profile update failed: ${e.message ?: "Unknown error"}")
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Helpers to reset state flags

    override fun onCleared() {
        repo.removeUserListener()
        super.onCleared()
    }
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