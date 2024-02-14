package com.zetsol.recipestatemanagement

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.zetsol.recipestatemanagement.databinding.ActivityEditProfileBinding
import kotlinx.coroutines.launch

class EditProfile : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    lateinit var appDatabase: AppDatabase
    private lateinit var userRepository: UserRepository
    private lateinit var userViewModel: UserViewModel

    private var selectedImageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appDatabase = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app-database"
        ).build()

        userRepository = UserRepository(appDatabase.userDao(), appDatabase.sessionDao())
        userViewModel = UserViewModel(userRepository)


        setUpViews()

        loadUserData()

        binding.buttonUpload.setOnClickListener{
            openImagePicker()

        }

    }

    private fun setUpViews() {
        binding.saveBtn.setOnClickListener {
            saveChanges()
        }
    }

    private fun loadUserData() {
        lifecycleScope.launch {
            val loggedInUser = userViewModel.getLoggedInUser()
            loggedInUser?.let { user ->
                binding.editTextName.setText(user.name)
                binding.editTextEmail.setText(user.email)
                //binding.imageViewProfile.setImageURI(Uri.parse(user.profilePictureUri))
                if (user.profilePicture != null) {
                    val bitmap = BitmapFactory.decodeByteArray(user.profilePicture, 0, user.profilePicture!!.size)
                    binding.imageViewProfile.setImageBitmap(bitmap)
                }

            }
        }
    }

    private fun saveChanges() {
        val newName = binding.editTextName.text.toString().trim()
        val newEmail = binding.editTextEmail.text.toString().trim()

        if (newName.isEmpty()) {
            binding.editTextName.error = "Name cannot be empty"
            return
        }

        if (newEmail.isEmpty()) {
            binding.editTextEmail.error = "Email cannot be empty"
            return
        }

        lifecycleScope.launch {
            val loggedInUser = userViewModel.getLoggedInUser()

            loggedInUser?.let { user ->
                try {
                    user.name = newName
                    user.email = newEmail
                    if (selectedImageUri != null) {
                        // Convert image to byte array
                        val inputStream = contentResolver.openInputStream(selectedImageUri!!)
                        val imageBytes = inputStream?.readBytes()

                        if (imageBytes != null) {
                            user.profilePicture = imageBytes // Save image as a byte array (BLOB)
                        }
                    }
                    userViewModel.updateUser(user)

                    Toast.makeText(this@EditProfile, "Changes Saved", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this@EditProfile, "Failed to save changes", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    selectedImageUri = uri
                    binding.imageViewProfile.setImageURI(uri)
                }
            }
        }
}