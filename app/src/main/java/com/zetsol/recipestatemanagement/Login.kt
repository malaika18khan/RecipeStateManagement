package com.zetsol.recipestatemanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.room.Room

class Login : AppCompatActivity() {

    lateinit var appDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Room Database
        appDatabase = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app-database"
        ).build()


        var btn_signup = findViewById<TextView>(R.id.signup_lp_btn)


        btn_signup.setOnClickListener(){
            val signup_intent = Intent(this, Signup::class.java)
            startActivity(signup_intent)
        }


    }
}