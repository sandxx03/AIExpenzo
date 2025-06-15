package com.example.aiexpenzo.data.repository

import com.example.aiexpenzo.data.model.User

object UserRepository {
    private val users = mutableListOf<User>()

    fun addUser(user: User): Boolean{
        if (users.any{ it.email == user.email}) return false // checks whether email is unique
        users.add(user)
        return true
    }

    fun validateUser(email: String, password: String): User?{
        return users.find { it.email == email && it.password == password }
    }
}