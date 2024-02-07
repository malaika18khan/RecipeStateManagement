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

class Signup : AppCompatActivity() {

    lateinit var appDatabase: AppDatabase
    private lateinit var userRepository: UserRepository
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Initialize Room Database
        appDatabase = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app-database"
        ).build()

        userRepository = UserRepository(appDatabase.userDao(), appDatabase.sessionDao())
        userViewModel = UserViewModel(userRepository)


        val etName = findViewById<EditText>(R.id.name_et)
        val etEmail = findViewById<EditText>(R.id.email_et)
        val etNumber = findViewById<EditText>(R.id.number_et)
        val etPassword = findViewById<EditText>(R.id.password_et)
        val btnSignup = findViewById<Button>(R.id.signup_btn)

        val tvErrorName = findViewById<TextView>(R.id.name_error_tv)
        val tvErrorEmail = findViewById<TextView>(R.id.email_error_tv)
        val tvErrorNumber = findViewById<TextView>(R.id.number_error_tv)
        val tvErrorPassword = findViewById<TextView>(R.id.password_error_tv)


        var btn_signin = findViewById<TextView>(R.id.signin_sp_btn)

        btn_signin.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        btnSignup.setOnClickListener {
            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val number = etNumber.text.toString()
            val password = etPassword.text.toString()

            if (name.isEmpty()) {

                tvErrorName.visibility = View.VISIBLE
                tvErrorName.setText("Name is empty")
            } else {
                tvErrorName.setText("")
                tvErrorName.visibility = View.GONE

            }

            if (email.isEmpty()){
                tvErrorEmail.visibility = View.VISIBLE
                tvErrorEmail.setText("Email is empty")
            } else if(!(email.contains('@')) && !(email.endsWith(".com"))){
                tvErrorEmail.setText("Your email is invalid")
            } else {
                tvErrorEmail.setText("")
                tvErrorEmail.visibility = View.GONE
            }

            if (number.isEmpty()){
                tvErrorNumber.visibility = View.VISIBLE
                tvErrorNumber.setText("Number is empty")
            } else if(number.length != 13 && !(number.startsWith("+92"))){
                tvErrorNumber.visibility = View.VISIBLE
                tvErrorNumber.setText("Please enter number in this format: +92__________")
            } else {
                tvErrorNumber.setText("")
                tvErrorNumber.visibility = View.GONE
            }

            if (password.isEmpty()){
                tvErrorPassword.visibility = View.VISIBLE
                tvErrorPassword.setText("Password is empty")
            } else {
                tvErrorPassword.setText("")
                tvErrorPassword.visibility = View.GONE

            }


            if (
                (tvErrorName.visibility == View.GONE) &&
                (tvErrorEmail.visibility == View.GONE) &&
                (tvErrorNumber.visibility == View.GONE) &&
                (tvErrorPassword.visibility == View.GONE)

            ) {

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        // Call the signUp function from the UserRepository
                        val user = userRepository.signUp(name, email, number, password)
                        user?.let {
                            // Fetch the user after signup using email and password
                            val loggedInUser = userViewModel.login(email, password)
                            loggedInUser?.let { user ->
                                // Start session for the newly signed up user
                                userViewModel.startSession(user)

                                // Switch to the main thread to update UI
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(applicationContext, "Signup succeeded", Toast.LENGTH_SHORT).show()

                                    // Handle navigation or any other action after successful registration
                                    val home = Intent(applicationContext, MainActivity::class.java)
                                    home.putExtra("userName", name)
                                    startActivity(home)
                                }
                            } ?: run {
                                // Handle the case when login after signup fails
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(applicationContext, "Failed to fetch user data after signup", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    } catch (e: Exception) {
                        // Handle any exceptions that might occur during signup or login
                        withContext(Dispatchers.Main) {
                            Toast.makeText(applicationContext, "Registration failed: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                        e.printStackTrace()
                    }
                }





            }
        }


    }
}