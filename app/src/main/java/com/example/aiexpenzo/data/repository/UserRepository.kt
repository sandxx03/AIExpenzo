package com.example.aiexpenzo.data.repository

import com.example.aiexpenzo.data.model.User

object UserRepository {
    private val users = mutableMapOf<String, User>()
    private var loggedInUser: User? = null

    fun addUser(user: User): Boolean{
        if (users.containsKey(user.email)) return false
        users[user.email] = user
        return true
    }

    fun validateUser(email: String, password: String): User?{
        val user = users[email]
        return if (user?.password == password) {
            loggedInUser = user
            user
        } else null
    }

    fun getLoggedInUser(): User? = loggedInUser

    fun setLoggedInUser(user:User){

        // Remove old user if email changed
        val oldEmail = loggedInUser?.email
        if (oldEmail != null && oldEmail != user.email) {
            users.remove(oldEmail)
        }
        if (user.email != oldEmail && users.containsKey(user.email)) return

        users[user.email] = user
        loggedInUser = user
    }

    fun clearLoggedInUser() {
        loggedInUser = null
    }
}