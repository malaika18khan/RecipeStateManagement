package com.zetsol.recipestatemanagement

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zetsol.recipestatemanagement.databinding.ItemRecipeBinding

class FavRecipesAdapter(private val context: Context) :
    ListAdapter<Recipe, FavRecipesAdapter.RecipeViewHolder>(RecipeDiffCallback()) {

    inner class RecipeViewHolder(private val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe) {
            binding.recipeNameTextView1.text = recipe.name
            binding.recipeTimeTextView1.text = recipe.cookingTime
            binding.recipeImageView1.setImageResource(recipe.imageUrl)

            binding.root.setOnClickListener {
                val intent = Intent(context, RecipeDetailsActivity::class.java).apply {
                    putExtra("recipe_id", recipe.id)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val currentRecipe = getItem(position)
        holder.bind(currentRecipe)
    }

    private class RecipeDiffCallback : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem == newItem
        }
    }
}
