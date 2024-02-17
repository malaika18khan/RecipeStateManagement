package com.zetsol.recipestatemanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Login : AppCompatActivity() {

    lateinit var appDatabase: AppDatabase
    private lateinit var userRepository: UserRepository
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Room Database
        appDatabase = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app-database"
        ).build()

        userRepository = UserRepository(appDatabase.userDao(), appDatabase.sessionDao())
        userViewModel = UserViewModel(userRepository)


        var btn_signup = findViewById<TextView>(R.id.signup_lp_btn)
        var btn_login = findViewById<Button>(R.id.login_btn)

        var email_et_lp = findViewById<EditText>(R.id.email_et_login)
        var password_et_lp = findViewById<EditText>(R.id.password_et_login)

        val emailErrorLoginTv = findViewById<TextView>(R.id.tv_error_email_login)
        val passwordErrorLoginTv = findViewById<TextView>(R.id.tv_error_password_login)


        btn_signup.setOnClickListener(){
            val signup_intent = Intent(this, Signup::class.java)
            startActivity(signup_intent)
        }

        btn_login.setOnClickListener {
            val email = email_et_lp.text.toString()
            val password = password_et_lp.text.toString()

            if (email.isEmpty()){
                emailErrorLoginTv.visibility = View.VISIBLE
                emailErrorLoginTv.setText("Email can not be empty")
            } else {
                emailErrorLoginTv.setText("")
                emailErrorLoginTv.visibility = View.GONE

            }

            if (password.isEmpty()){
                passwordErrorLoginTv.visibility = View.VISIBLE
                passwordErrorLoginTv.setText("Password can not be empty")
            } else {
                passwordErrorLoginTv.setText("")
                passwordErrorLoginTv.visibility = View.GONE

            }

            if (
                (emailErrorLoginTv.visibility == View.GONE) &&
                (passwordErrorLoginTv.visibility == View.GONE)
            ) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        // Call the login function from the UserViewModel
                        val loggedInUser = userViewModel.login(email, password)

                        if (loggedInUser != null) {
                            // Start session for the logged-in user
                            userViewModel.startSession(loggedInUser)

                            // Redirect to the main screen
                            withContext(Dispatchers.Main) {
                                val homeIntent = Intent(applicationContext, MainActivity::class.java)
                                homeIntent.putExtra("userName", loggedInUser.name)
                                startActivity(homeIntent)
                                finish()
                            }
                        } else {
                            // Login failed, show an error message
                            withContext(Dispatchers.Main) {
//                                Toast.makeText(
//                                    applicationContext,
//                                    "Login failed. Please check your credentials.",
//                                    Toast.LENGTH_SHORT
//                                ).show()
                                "Login failed. Please check your credentials.".showToast(applicationContext)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        // Handle any exception that might occur during login
                        withContext(Dispatchers.Main) {
//                            Toast.makeText(
//                                applicationContext,
//                                "An error occurred during login: ${e.message}",
//                                Toast.LENGTH_SHORT
//                            ).show()
                            "An error occurred during login: ${e.message}".showToast(applicationContext)
                        }
                    }
                }
            }
        }
    }

}