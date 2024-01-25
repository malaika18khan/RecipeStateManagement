package com.zetsol.recipestatemanagement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.room.Room

class Signup : AppCompatActivity() {

    lateinit var appDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Initialize Room Database
        appDatabase = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app-database"
        ).build()


        val etName = findViewById<EditText>(R.id.name_et)
        val etEmail = findViewById<EditText>(R.id.email_et)
        val etNumber = findViewById<EditText>(R.id.number_et)
        val etPassword = findViewById<EditText>(R.id.password_et)
        //val cbTerms = findViewById<CheckBox>(R.id.terms_cb)
        val btnSignup = findViewById<Button>(R.id.signup_btn)

        val tvErrorName = findViewById<TextView>(R.id.name_error_tv)
        val tvErrorEmail = findViewById<TextView>(R.id.email_error_tv)
        val tvErrorNumber = findViewById<TextView>(R.id.number_error_tv)
        val tvErrorPassword = findViewById<TextView>(R.id.password_error_tv)


        var btn_signin = findViewById<TextView>(R.id.signin_sp_btn)

    }
}