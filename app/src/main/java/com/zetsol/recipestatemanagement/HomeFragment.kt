package com.zetsol.recipestatemanagement

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Room
import com.google.android.material.appbar.MaterialToolbar
import com.zetsol.recipestatemanagement.databinding.ActivityMainBinding
import com.zetsol.recipestatemanagement.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    lateinit var appDatabase: AppDatabase
    private lateinit var recipeRepository: RecipeRepository
    private lateinit var recipeViewModel: RecipeViewModel
    private lateinit var recipeAdapter: RecipeAdapter

    private lateinit var userRepository: UserRepository
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        // Initialize Room Database
        appDatabase = Room.databaseBuilder(
            requireContext().applicationContext,
            AppDatabase::class.java,
            "app-database"
        ).build()

        recipeRepository = RecipeRepository(appDatabase.recipeDao())
        recipeViewModel = RecipeViewModel(recipeRepository)

        userRepository = UserRepository(appDatabase.userDao(), appDatabase.sessionDao())
        userViewModel = UserViewModel(userRepository)


        val toolbar: MaterialToolbar? = view?.findViewById(R.id.materialToolbar)
        toolbar.let {
            (requireActivity() as AppCompatActivity).setSupportActionBar(it)
        }

        lifecycleScope.launch {
            val loggedInUser = userViewModel.getLoggedInUser()
            loggedInUser?.let { user ->
                user.profilePicture?.let { imageBytes ->
                    try {
                        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        toolbar?.findViewById<ImageView>(R.id.user_image)?.setImageBitmap(bitmap)
                    } catch (e: Exception) {
                        Log.e("HomeFragment", "Error decoding profile picture: ${e.message}")
                    }
                }
            }
        }


        recipeAdapter = RecipeAdapter(this)
        binding.recipesRv.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2)
            adapter = recipeAdapter
        }

        recipeViewModel.allRecipes.observe(viewLifecycleOwner) { recipes ->
            recipes?.let {
                Log.d("MainActivity", "Received ${recipes.size} recipes")
                recipeAdapter.updateRecipes(it)
            }
        }

        return view
    }



}

