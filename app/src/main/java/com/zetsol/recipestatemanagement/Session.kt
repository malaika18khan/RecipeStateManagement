package com.zetsol.recipestatemanagement

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sessions")
data class Session(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String,
    val isLoggedIn: Boolean
)
