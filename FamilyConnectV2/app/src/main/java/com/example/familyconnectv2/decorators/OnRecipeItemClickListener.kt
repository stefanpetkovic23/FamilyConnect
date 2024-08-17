package com.example.familyconnectv2.decorators

import com.example.familyconnectv2.sqlBudget.Recipe

interface OnRecipeItemClickListener {
    fun onRecipeItemClick(recipe: Recipe)
}