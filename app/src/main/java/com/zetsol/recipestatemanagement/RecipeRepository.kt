package com.zetsol.recipestatemanagement

import android.util.Log
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipeRepository(private val recipeDao: RecipeDao) {

    val allRecipes: LiveData<List<Recipe>> = recipeDao.getAllRecipes()
    val favRecipes: LiveData<List<Recipe>> = recipeDao.getFavoriteRecipes()

    suspend fun insert(recipe: Recipe) {
        recipeDao.insert(recipe)
    }

    suspend fun update(recipe: Recipe) {
        withContext(Dispatchers.IO) {
            recipeDao.update(recipe)
        }
    }

    suspend fun delete(recipe: Recipe) {
        recipeDao.delete(recipe)
    }

    suspend fun getRecipeById(recipeId: Long): Recipe? {
        return withContext(Dispatchers.IO) {
            recipeDao.getRecipeById(recipeId)
        }
    }

}