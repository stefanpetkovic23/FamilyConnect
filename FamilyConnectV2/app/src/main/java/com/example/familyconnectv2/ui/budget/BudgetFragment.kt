package com.example.familyconnectv2.ui.budget

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.familyconnectv2.R
import com.example.familyconnectv2.adapters.RecipeAdapter
import com.example.familyconnectv2.databinding.FragmentSlideshowBinding
import com.example.familyconnectv2.decorators.OnRecipeItemClickListener
import com.example.familyconnectv2.sqlBudget.BudgetDatabaseHelper
import com.example.familyconnectv2.sqlBudget.Recipe
import com.example.familyconnectv2.sqlBudget.SharedRecipeViewModel
import com.example.familyconnectv2.ui.chatmassages.ChatMessagesViewModel

class BudgetFragment : Fragment(), OnRecipeItemClickListener {

    private var _binding: FragmentSlideshowBinding? = null
    private lateinit var budgetDatabaseHelper: BudgetDatabaseHelper
    private lateinit var viewModel: SlideshowViewModel
    private lateinit var adapter: RecipeAdapter
    private val sharedRecipeViewModel: SharedRecipeViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       viewModel =
            ViewModelProvider(this).get(SlideshowViewModel::class.java)



        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        adapter = RecipeAdapter(emptyList(),this) // Početna prazna lista recepata
        binding.recyclerViewCategories.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCategories.adapter = adapter

        viewModel.fetchRecipes()

        viewModel.recipes.observe(viewLifecycleOwner) { recipes ->
            adapter.setData(recipes)
        }



        return root
    }

    override fun onRecipeItemClick(recipe: Recipe) {
        // Otvorite fragment sa detaljima recepta i prosledite odabrani recept
        sharedRecipeViewModel.selectedRecipe = recipe
        Log.d("BudgetFragment", "Selected recipe: $recipe")
        findNavController().navigate(R.id.action_nav_slideshow_to_recipesDetailsFragment)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}