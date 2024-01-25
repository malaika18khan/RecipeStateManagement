package com.zetsol.recipestatemanagement

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String,
    var email: String,
    var number: String,
    val password: String,
    val favoriteRecipes: String,
    var isLoggedIn: Boolean = false
)
