package com.example.familyconnectv2.ui.budget

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.familyconnectv2.sqlBudget.Recipe
import com.google.firebase.database.*


class SlideshowViewModel : ViewModel() {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance("https://familyconnect-adf88-default-rtdb.europe-west1.firebasedatabase.app/")
    private val recipesReference: DatabaseReference = database.getReference("Recipes")

    private val _recipes: MutableLiveData<List<Recipe>> = MutableLiveData()
    val recipes: LiveData<List<Recipe>> = _recipes

    fun fetchRecipes() {
        recipesReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val recipeList = mutableListOf<Recipe>()
                for (recipeSnapshot in snapshot.children) {
                    val recipe = recipeSnapshot.getValue(Recipe::class.java)
                    recipe?.let { recipeList.add(it) }
                }
                _recipes.value = recipeList

                Log.d(TAG, "Retrieved recipes: $recipeList")
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

}