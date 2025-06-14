package com.example.aiexpenzo.viewmodel

import androidx.lifecycle.ViewModel // Android's ViewModel class - store and manage UI related data
import com.example.aiexpenzo.data.model.User
import com.example.aiexpenzo.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow // StateFlow in Kotlin coroutines - reactive state management
import kotlinx.coroutines.flow.asStateFlow
import android.util.Patterns

class AuthViewModel:ViewModel() {

    // private mutable state flow that holds authentication status (default = false)
    private val _authSuccess = MutableStateFlow(false)

    // public read-only state flow exposed to UI layer to observe authentication success
    val authSuccess: StateFlow<Boolean> = _authSuccess.asStateFlow()

    // private mutablle state flow that holds error messages (default = null)
    private val _errorMessage = MutableStateFlow<String?>(null)

    // public read-only state flow exposed to UI layer to observe errors
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Function: Sign Up - handles user registration
    fun signUp(name: String, email:String, password: String){
        // Validation check: If any field is empty, sets error message & authSuccess to false
        if (name.isBlank() || email.isBlank() || password.isBlank()){
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

        // if valid user returned, authSuccess = true
        if (user != null){
            _authSuccess.value = true
        } else {
            // Else if user invalid, sets error message & authSuccess = false
            _errorMessage.value = "Incorrect login info."
            _authSuccess.value = false
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