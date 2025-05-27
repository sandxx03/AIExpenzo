package com.example.aiexpenzo.data.repository

import com.example.aiexpenzo.data.model.User

//Handles sign up and login through interaction with database
// TO BE REPLACED WITH FIRESTORE
class AuthRepository {

    private val users=mutableListOf<User>() //temporary list to store users in memory

    //check if user with the same email already exists, if not, add new user and return true
    fun registerUser(user: User):Boolean{
        if (users.any{it.email == user.email}) return false
        users.add(user)
        return true

    }

    //look for user whose email and password match the input, returns true if found, else false
    fun loginUser(email:String, password:String):Boolean{
        return users.any{it.email == email && it.password == password}
    }
}