package com.zetsol.recipestatemanagement

import androidx.lifecycle.ViewModel

import androidx.lifecycle.LiveData
//import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    val allUsers: LiveData<List<User>> = userRepository.getAllUsers()

    suspend fun login(email: String, password: String): User? {
        return userRepository.login(email, password)
    }

    suspend fun signUp(name: String, email: String, number: String, password: String) {
        userRepository.signUp(name, email, number, password)
    }

    suspend fun startSession(loggedInUser: User): Session {
        val session = userRepository.startSession(loggedInUser)
        return session
    }

    suspend fun endSession(userId: String) {
        userRepository.endSession(userId)
    }

    suspend fun checkSession(userId: String): Boolean {
        return userRepository.checkSession(userId)
    }

    suspend fun getLoggedInUser(): User? {
        return userRepository.getLoggedInUser()
    }

    suspend fun getActiveSession(): Session? {
        return userRepository.getActiveSession()
    }
}
