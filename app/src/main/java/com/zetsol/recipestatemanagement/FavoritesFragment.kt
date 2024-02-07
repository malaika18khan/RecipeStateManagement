package com.zetsol.recipestatemanagement

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.zetsol.recipestatemanagement.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {

    lateinit var appDatabase: AppDatabase
    private lateinit var recipeRepository: RecipeRepository
    private lateinit var recipeViewModel: RecipeViewModel


    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var favRecipesAdapter: FavRecipesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Room Database
        appDatabase = Room.databaseBuilder(
            requireContext().applicationContext,
            AppDatabase::class.java,
            "app-database"
        ).build()

        recipeRepository = RecipeRepository(appDatabase.recipeDao())
        recipeViewModel = RecipeViewModel(recipeRepository)

        favRecipesAdapter = FavRecipesAdapter(requireContext())

        binding.favRecipesRv.apply {
            adapter = favRecipesAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }

        recipeViewModel.favRecipes.observe(viewLifecycleOwner) { recipes ->
            recipes?.let {

                if (recipes.isEmpty()) {
                Log.d("FavoritesFragment", "Favorite recipes list is empty")
                binding.textViewEmptyState.visibility = View.VISIBLE
            } else {
                Log.d("FavoritesFragment", "Favorite recipes list is not empty")
                binding.textViewEmptyState.visibility = View.GONE
                favRecipesAdapter.submitList(recipes)
            }
            }
        }


    }
}
