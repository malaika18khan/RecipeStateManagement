package com.zetsol.recipestatemanagement

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.zetsol.recipestatemanagement.databinding.FragmentProfileBinding
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {

    lateinit var appDatabase: AppDatabase
    private lateinit var userRepository: UserRepository
    private lateinit var userViewModel: UserViewModel

    private lateinit var binding: FragmentProfileBinding
    private lateinit var profileImageView: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var editProfileButton: TextView
    private lateinit var logoutButton: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        // Initialize Room Database
        appDatabase = Room.databaseBuilder(
            requireContext().applicationContext,
            AppDatabase::class.java,
            "app-database"
        ).build()

        userRepository = UserRepository(appDatabase.userDao(), appDatabase.sessionDao())
        userViewModel = UserViewModel(userRepository)

        profileImageView = view.findViewById(R.id.profile_image)
        nameTextView = view.findViewById(R.id.text_name)
        emailTextView = view.findViewById(R.id.text_email)
        editProfileButton = view.findViewById(R.id.btn_edit_profile)
        logoutButton = view.findViewById(R.id.btn_logout)


        lifecycleScope.launch {
            val loggedInUser = userViewModel.getLoggedInUser()
            loggedInUser?.let {
                nameTextView.text = it.name
                emailTextView.text = it.email

                if (it.profilePicture != null) {
                    val bitmap = BitmapFactory.decodeByteArray(it.profilePicture, 0, it.profilePicture!!.size)
                    binding.profileImage.setImageBitmap(bitmap)
                }
            }
        }

        logoutButton.setOnClickListener{

            lifecycleScope.launch {
                val loggedInUser = userViewModel.getLoggedInUser()
                loggedInUser?.let {
                    userViewModel.endSession(it.userId.toString())
                    val logoutIntent = Intent(activity, Login::class.java)
                    startActivity(logoutIntent)
                    activity?.finish()
                }
            }
        }

        editProfileButton.setOnClickListener {
            val profileIntent = Intent(requireContext(), EditProfile::class.java)
            startActivity(profileIntent)
        }

        // Inflate the layout for this fragment
        return view
    }


}