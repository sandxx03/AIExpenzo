package com.example.aiexpenzo.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.example.aiexpenzo.data.model.User
import com.example.aiexpenzo.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel:ViewModel() {

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

    // Function: Sign Up - handles user registration
    fun signUp(name: String, email:String, password: String){
        // Validation check: If any field is empty, sets error message & authSuccess to false
        val validationError = validateProfileInput(name, email, password)
        if (validationError != null){
            _errorMessage.value = validationError
            _authSuccess.value = false
            return

        }
        val user = User(name, email, password)   // Creates new User object with the attributes
        val result = UserRepository.addUser(user)   // Calls repository to add user

        // If user is registered in repository successfully, authSuccess = true
        if (result){
            _authSuccess.value = true
        } else {
            // Else if email already exists, sets error message & authSuccess = false
            _errorMessage.value = "Email is already registered."
            _authSuccess.value = false
        }
    }

    // Function: Login
    fun login(email: String, password: String){
        // Validation check: If any field is empty, sets error message & authSuccess to false
        if (email.isBlank() || password.isBlank()){
            _errorMessage.value = "Please fill in all the fields."
            _authSuccess.value = false
            return  // returns early if validation fails
        }

        // Validation check: If email format is incorrect, sets error message & authSuccess to false
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            _errorMessage.value="Please enter a valid email address."
            _authSuccess.value = false
            return
        }

        // Validation check: If password length < 6 characters, sets error message & authSuccess to false
        if (password.length <6){
            _errorMessage.value = "Password must be at least 6 characters."
            _authSuccess.value = false
            return
        }

        val user = UserRepository.validateUser(email, password) // Calls the repository's validateUser function to validate user credentials
        _currentUser.value =  user

        // if valid user returned, authSuccess = true
        if (user != null){
            _authSuccess.value = true
        } else {
            // Else if user invalid, sets error message & authSuccess = false
            _errorMessage.value = "Incorrect login info."
            _authSuccess.value = false
        }


    }

    // Function: Set Monthly Income
    fun setMonthlyIncome(income: Float){
        val user = _currentUser.value ?: UserRepository.getLoggedInUser()
        if (user != null){
            val updated = user.copy(monthlyIncome = income)
            UserRepository.setLoggedInUser(updated)
            _currentUser.value = updated
        }
    }

    // Function: Set Monthly Budget
    fun setMonthlyBudget(budget: Float){
        val user = _currentUser.value ?: UserRepository.getLoggedInUser()
        if (user != null){
            val updated = user.copy(monthlyBudget = budget)
            UserRepository.setLoggedInUser(updated)
            _currentUser.value = updated
        }
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
        val user = _currentUser.value ?: return
        val updatedUser = user.copy(name = newName)
        _currentUser.value = updatedUser
        UserRepository.setLoggedInUser(updatedUser)
    }

    // Function: Update Email
    fun updateEmail(newEmail: String){
        val user = _currentUser.value ?: return
        val updatedUser = user.copy(email = newEmail)
        _currentUser.value = updatedUser
        UserRepository.setLoggedInUser(updatedUser)
    }

    // Function: Update Password
    fun updatePassword(newPassword: String){
        val user = _currentUser.value ?: return
        val updatedUser = user.copy(password = newPassword)
        _currentUser.value = updatedUser
        UserRepository.setLoggedInUser(updatedUser)
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

    // Function - validate value of monthly budget (should not be more than income)
    fun isBudgetValid(budget: Float): Boolean{
        val user = _currentUser.value ?: UserRepository.getLoggedInUser()
        val income = user?.monthlyIncome ?: 0f
        return budget <= income
    }

    // Function - log users out
    fun logout(){
        _currentUser.value = null
        _authSuccess.value = false
        UserRepository.clearLoggedInUser()
    }




}