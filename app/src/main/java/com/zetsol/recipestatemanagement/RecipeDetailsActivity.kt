package com.zetsol.recipestatemanagement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room

class RecipeDetailsActivity : AppCompatActivity() {

    lateinit var appDatabase: AppDatabase
    private lateinit var recipeRepository: RecipeRepository
    private lateinit var recipeViewModel: RecipeViewModel
    private var recipeId: Long = -1
    private lateinit var recipe: Recipe

    private lateinit var recipeNameTv: TextView
    private lateinit var recipeIngredientsTextView: TextView
    private lateinit var recipeCookingTimeTextView: TextView
    private lateinit var recipeInstructionsTextView: TextView
    private lateinit var recipeImage: ImageView
    private lateinit var favBtn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)

        // Initialize Room Database
        appDatabase = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app-database"
        ).build()

        recipeRepository = RecipeRepository(appDatabase.recipeDao())
        recipeViewModel = RecipeViewModel(recipeRepository)
        recipeNameTv = findViewById(R.id.recipeNameTv)
        recipeIngredientsTextView = findViewById(R.id.foodIngredientsTv)
        recipeCookingTimeTextView = findViewById(R.id.cookingTimeTv)
        recipeIngredientsTextView = findViewById(R.id.foodIngredientsTv)
        recipeInstructionsTextView = findViewById(R.id.cookingInstructionsTv)
        recipeImage = findViewById(R.id.recipeImageView)
        favBtn = findViewById(R.id.favorite_btn)

        recipeId = intent.getLongExtra("recipe_id", -1)


        if (recipeId != -1L) {
            recipe = recipeViewModel.getRecipeById(recipeId)!!
            observeRecipe()
        } else {
            Toast.makeText(this, "Recipe not provided", Toast.LENGTH_SHORT).show()
            finish()
        }

        favBtn.setOnClickListener {

            // Toggle favorite status
            recipe.favorite = !recipe.favorite
            recipeViewModel.updateRecipe(recipe)
            updateFavoriteUI(recipe.favorite)


        }


    }


    private fun observeRecipe() {

        recipeNameTv.text = recipe.name
        recipeIngredientsTextView.text = recipe.ingredients
        recipeCookingTimeTextView.text = recipe.cookingTime
        recipeInstructionsTextView.text = recipe.instructions
        recipeImage.setImageResource(recipe.imageUrl)
        updateFavoriteUI(recipe.favorite)

    }

    private fun updateFavoriteUI(isFavorite: Boolean) {
        favBtn.setImageResource(if (isFavorite) R.drawable.heart_filled else R.drawable.heart)
        Log.d("RecipeDetailsActivity", "UI updated with favorite status: $isFavorite")
    }


}