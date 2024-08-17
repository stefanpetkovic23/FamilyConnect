package com.example.familyconnectv2.ui.budget

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.familyconnectv2.R
import com.example.familyconnectv2.sp.SharedViewModel
import com.example.familyconnectv2.sqlBudget.Recipe
import com.example.familyconnectv2.sqlBudget.SharedRecipeViewModel

class RecipesDetailsFragment : Fragment() {

    private val sharedRecipeViewModel: SharedRecipeViewModel by activityViewModels()

    companion object {
        fun newInstance() {}
    }



    private lateinit var viewModel: RecipesDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_recipes_details, container, false)



        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipe = sharedRecipeViewModel.selectedRecipe
        if (recipe != null) {
            Log.d("RecipeDetailsFragment", "Received recipe: $recipe")
        } else {
            Log.d("RecipeDetailsFragment", "No recipe received")
        }
        val imageRecipe = view.findViewById<ImageView>(R.id.imageRecipe)
        // Učitaj sliku recepta ako postoji
        recipe?.image?.let {
            Glide.with(requireContext())
                .load(it)
                .into(imageRecipe)
        }

        // Postavljanje sastojaka
        val layoutIngredients = view.findViewById<LinearLayout>(R.id.layoutIngredients)
        recipe?.ingredients?.let {
            val ingredientsList = it.split(", ")
            for (ingredient in ingredientsList) {
                val ingredientTextView = TextView(requireContext())
                ingredientTextView.text = "• $ingredient"
                layoutIngredients.addView(ingredientTextView)
            }
        }

        val layoutPreparation = view.findViewById<LinearLayout>(R.id.layoutInstruction)
        recipe?.priprema?.let { preparation ->
            val preparationSteps = preparation.split(". ")
            for ((index, step) in preparationSteps.withIndex()) {
                val numberedStep = "${index + 1}. $step"
                val preparationTextView = TextView(requireContext())
                preparationTextView.text = numberedStep

                // Dodavanje margine za svaki korak pripreme
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(0, 8, 0, 0) // Postavite željene vrednosti margine
                preparationTextView.layoutParams = params

                layoutPreparation.addView(preparationTextView)
            }
        }

        val textPortion = view.findViewById<TextView>(R.id.textPortion)
        textPortion.text = "${recipe?.servings ?: "N/A"}"

        // Postavi vreme pripreme
        val textTime = view.findViewById<TextView>(R.id.textTime)
        textTime.text = " ${recipe?.prep_time ?: "N/A"}"

        // Postavi kalorije
        val textCalories = view.findViewById<TextView>(R.id.textCalories)
        recipe?.nutrition?.let {

            textCalories.text = recipe.nutrition
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RecipesDetailsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}