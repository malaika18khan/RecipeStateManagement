package com.zetsol.recipestatemanagement

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RecipeDao {

    @Insert
    suspend fun insert(recipe: Recipe)

    @Update
    suspend fun update(recipe: Recipe)

    @Delete
    suspend fun delete(recipe: Recipe)

    @Query("SELECT * FROM recipes")
    fun getAllRecipes(): LiveData<List<Recipe>>


    @Query("SELECT * FROM recipes WHERE favorite = 1")
    fun getFavoriteRecipes(): LiveData<List<Recipe>>


    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    fun getRecipeById(recipeId: Long): Recipe?



}