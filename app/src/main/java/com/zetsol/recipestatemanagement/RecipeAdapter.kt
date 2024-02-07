package com.zetsol.recipestatemanagement

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet.Constraint
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class RecipeAdapter(private val fragment: Fragment) :
    RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {


        private val recipeList = mutableListOf<Recipe>()

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeImageView: ImageView = itemView.findViewById(R.id.recipeImageView1)
        val recipeNameTextView: TextView = itemView.findViewById(R.id.recipeNameTextView1)
        val recipeTimeTextView: TextView = itemView.findViewById(R.id.recipeTimeTextView1)
        val recipeItem: ConstraintLayout = itemView.findViewById(R.id.recipeItemSample)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipe, parent, false)
        return RecipeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {

        val recipe: Recipe = recipeList.get(position)
        holder.recipeNameTextView.text = recipe.name
        holder.recipeTimeTextView.text = recipe.cookingTime
        holder.recipeImageView.setImageResource(recipe.imageUrl)

        holder.recipeItem.setOnClickListener{

                val selectedRecipeId = recipeList[position].id
                val intent = Intent(fragment.requireContext(), RecipeDetailsActivity::class.java).apply {
                    putExtra("recipe_id", selectedRecipeId)
                }
                fragment.requireContext().startActivity(intent)


        }

    }

    override fun getItemCount(): Int {
        return recipeList.size
    }


    fun updateRecipes(newRecipes: List<Recipe>) {
        recipeList.clear()
        recipeList.addAll(newRecipes)
        notifyDataSetChanged()
    }

}
