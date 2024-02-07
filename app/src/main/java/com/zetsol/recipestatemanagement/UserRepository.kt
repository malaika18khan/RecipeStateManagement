package com.zetsol.recipestatemanagement

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData

class UserRepository(private val userDao: UserDao, private val sessionDao: SessionDao) {

    fun getAllUsers(): LiveData<List<User>> {
        return userDao.getAllUsers()
    }

    suspend fun login(email: String, password: String): User? {
        return userDao.login(email, password)
    }

    suspend fun signUp(name: String, email: String, number: String, password: String) {
        val newUser = User(name = name, email = email, number = number, password = password, isLoggedIn = true)
        userDao.insert(newUser)
    }

    suspend fun startSession(loggedInUser: User) :Session{
        //insert a session record into the Session table
        val session = Session(userId = loggedInUser.userId.toString(), isLoggedIn = true)
        sessionDao.insert(session)
        Log.d("SessionManagement", "Session inserted: $session")
       // Toast.makeText(appl, "Session inserted: $session", Toast.LENGTH_LONG).show()
        return session
    }

    suspend fun endSession(userId: String) {
        sessionDao.clearSession(userId)
    }

    suspend fun checkSession(userId: String): Boolean {
        val session = sessionDao.getSession(userId)
        return session?.isLoggedIn == true
    }

    suspend fun getLoggedInUser(): User? {
        return userDao.getLoggedInUser()
    }

    suspend fun getActiveSession(): Session? {
        return sessionDao.getActiveSession()
    }
}
