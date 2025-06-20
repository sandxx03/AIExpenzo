package com.example.aiexpenzo.data.repository

import com.example.aiexpenzo.data.model.User

object UserRepository {
    private val users = mutableListOf<User>()
    private var loggedInUser: User? = null

    fun addUser(user: User): Boolean{
        if (users.any{ it.email == user.email}) return false // checks whether email is unique
        users.add(user)
        return true
    }

    fun validateUser(email: String, password: String): User?{
        val user = return users.find { it.email == email && it.password == password }
        loggedInUser = user
        return user
    }

    fun getLoggedInUser(): User? = loggedInUser

    fun setLoggedInUser(user:User){
        loggedInUser = user
        //Update list in mock
        users.replaceAll{if (it.email == user.email) user else it}
    }
}