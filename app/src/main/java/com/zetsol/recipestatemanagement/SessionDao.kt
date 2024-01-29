package com.zetsol.recipestatemanagement

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SessionDao {

    @Insert
    suspend fun insert(session: Session)

    @Query("SELECT * FROM sessions WHERE userId = :userId LIMIT 1")
    suspend fun getSession(userId: String): Session?

    @Query("SELECT * FROM sessions WHERE isLoggedIn = 1 LIMIT 1")   // For session management
    suspend fun getActiveSession(): Session?


    @Query("DELETE FROM sessions WHERE userId = :userId")
    suspend fun clearSession(userId: String)
}