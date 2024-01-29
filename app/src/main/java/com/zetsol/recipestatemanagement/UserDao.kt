package com.zetsol.recipestatemanagement

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {

    @Insert
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM users")
    fun getAllUsers(): LiveData<List<User>>

    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    fun login(email: String, password: String): User?


    @Query("SELECT * FROM users WHERE userId = :userId")
    suspend fun getUserById(userId: Int): User?

    @Query("SELECT * FROM users WHERE isLoggedIn = 1 LIMIT 1")   // For session management
    suspend fun getLoggedInUser(): User?

    @Query("SELECT * FROM recipes WHERE favorite = 1 AND id IN (SELECT recipeId FROM favorite_recipes WHERE userId = :userId)")
    suspend fun getFavoriteRecipes(userId: Int): List<Recipe>

    @Insert
    suspend fun insertFavoriteRecipe(favoriteRecipe: FavoriteRecipe)

    @Delete
    suspend fun deleteFavoriteRecipe(favoriteRecipe: FavoriteRecipe)
}