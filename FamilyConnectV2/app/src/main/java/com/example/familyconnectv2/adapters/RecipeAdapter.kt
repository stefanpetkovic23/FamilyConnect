package com.example.familyconnectv2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.familyconnectv2.R
import com.example.familyconnectv2.decorators.OnRecipeItemClickListener
import com.example.familyconnectv2.models.CalendarActivity
import com.example.familyconnectv2.sqlBudget.Recipe

class RecipeAdapter (private var recipes: List<Recipe>,private val itemClickListener: OnRecipeItemClickListener) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipeitem, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.bind(recipe)
    }

    fun setData(newData: List<Recipe>) {
        recipes= newData
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val recipeImage: ImageView = itemView.findViewById(R.id.itempic)
        private val recipeTitle: TextView = itemView.findViewById(R.id.recycletitle)
        private val recipeTime: TextView = itemView.findViewById(R.id.itemtime)
        private val recipeServings: TextView = itemView.findViewById(R.id.itemserving)
        private val imageLevel: ImageView = itemView.findViewById(R.id.imagelevel)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val recipe = recipes[position]
                itemClickListener.onRecipeItemClick(recipe)
            }
        }

        fun bind(recipe: Recipe) {
            Glide.with(itemView.context).load(recipe.image).into(recipeImage)
            recipeTitle.text = recipe.naziv
            recipeTime.text = recipe.prep_time
            recipeServings.text = recipe.servings.toString()

            if(recipe.tezina == "Medium")
            {
                imageLevel.setBackgroundResource(R.drawable.medium)
            }
            else if(recipe.tezina == "Hard")
            {
                imageLevel.setBackgroundResource(R.drawable.letter_h)
            }
            else
            {
                imageLevel.setBackgroundResource(R.drawable.letter_e)
            }
        }
    }
}