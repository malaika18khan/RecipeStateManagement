package com.zetsol.recipestatemanagement

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    var userId: Long = 0,
    var name: String,
    var email: String,
    var number: String,
    val password: String,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var profilePicture: ByteArray? = null,
    var isLoggedIn: Boolean = false
)
