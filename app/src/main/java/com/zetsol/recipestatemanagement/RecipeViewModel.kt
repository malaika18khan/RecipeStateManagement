package com.zetsol.recipestatemanagement

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class RecipeViewModel(private val recipeRepository: RecipeRepository) : ViewModel() {

    val allRecipes: LiveData<List<Recipe>> = recipeRepository.allRecipes
    val favRecipes: LiveData<List<Recipe>> = recipeRepository.favRecipes

    fun insert(recipe: Recipe) {
        viewModelScope.launch {
            recipeRepository.insert(recipe)
        }
    }

    fun getRecipeById(recipeId: Long): Recipe? {
        return runBlocking {
            recipeRepository.getRecipeById(recipeId)
        }
    }

    fun updateRecipe(recipe: Recipe) {
        viewModelScope.launch {
            recipeRepository.update(recipe)
        }
    }

}