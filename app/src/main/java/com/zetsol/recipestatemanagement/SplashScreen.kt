package com.zetsol.recipestatemanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SplashScreen : AppCompatActivity() {

    lateinit var appDatabase: AppDatabase
    private lateinit var userRepository: UserRepository
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Initialize Room Database
        appDatabase = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app-database"
        ).build()

        userRepository = UserRepository(appDatabase.userDao(), appDatabase.sessionDao())
        userViewModel = UserViewModel(userRepository)


        Handler().postDelayed({
            CoroutineScope(Dispatchers.Main).launch {

                val active_session = userViewModel.getActiveSession()

                if (active_session != null) {

                    val activeUser = userViewModel.getLoggedInUser()
                    // User is logged in, navigate to the main activity
                    val main_intent = Intent(this@SplashScreen, MainActivity::class.java)
                    if (activeUser != null) {
                        main_intent.putExtra("userName", activeUser.name)
                    }
                    startActivity(main_intent)
                } else {
                    // User is not logged in, navigate to the login screen
                    val login_intent = Intent(this@SplashScreen, Login::class.java)
                    startActivity(login_intent)
                }
                finish()
            }
        }, 3000)
    }
}
