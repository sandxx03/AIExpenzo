package com.example.aiexpenzo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aiexpenzo.data.model.User
import com.example.aiexpenzo.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class AuthViewModel : ViewModel() { //ViewModel - keeps state even when the screen rotates
    private val repository = AuthRepository()
    private val _authSuccess = MutableStateFlow(false)  //MutableStateFlow - Holds value that Compose can observe
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage
    val authSuccess: StateFlow<Boolean> = _authSuccess

    //Checks email format, register user
    fun signUp(name: String, email: String, password: String){
        viewModelScope.launch{ //ViewModelScope.launch - Runs async code without blocking UI
            if (!isValidEmail(email)){
                _errorMessage.value = "Invalid email address"
                return@launch
            }
            val success = repository.registerUser(User(name,email,password))
            if(success){
                _authSuccess.value=true
                }else{
                    _errorMessage.value = "Email already registered!"
            }

        }
    }

    //Validates credentials
    fun login(email:String, password: String){
        viewModelScope.launch {
            if(!isValidEmail(email)){
                _errorMessage.value = "Invalid email address"
                return@launch
            }
            val success = repository.loginUser(email,password)
            if(success){
                _authSuccess.value=true
            }else{
                _errorMessage.value = "Invalid email or password!"
            }
        }
    }

    //Clears success flag after navigated
    fun resetAuthStatus() {
        _authSuccess.value = false
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

    }

    //Clears error message after pop up
    fun clearError(){
        _errorMessage.value = null
    }
}





