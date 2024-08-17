package com.example.familyconnectv2.ui.shopping

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.familyconnectv2.models.Group
import com.example.familyconnectv2.models.ShoppingGroup
import com.example.familyconnectv2.models.ShoppingItemRequest
import com.example.familyconnectv2.models.ShoppingList
import com.example.familyconnectv2.retrofit.RetrofitInstance
import com.example.familyconnectv2.sp.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShoppingViewModel : ViewModel() {

    private val apiService = RetrofitInstance.api

    private lateinit var tokenManager: TokenManager

    private val _shoppingItems = MutableLiveData<List<ShoppingGroup>>()
    val shoppingItems: LiveData<List<ShoppingGroup>>
        get() = _shoppingItems

    fun initTokenManager(context: Context) {
        val sharedPreferences = context.getSharedPreferences("Token", Context.MODE_PRIVATE)
        tokenManager = TokenManager(sharedPreferences)
    }

    fun addItemToShoppingList(groupId: String, itemName: String, quantity: Int, isCompleted: Boolean) {
        val jwtToken = tokenManager.getToken()
        val request = ShoppingItemRequest(groupId, itemName, quantity, isCompleted)

        apiService.addItemToShoppingListInGroup("Bearer $jwtToken", request)
            .enqueue(object : Callback<Group> {
                override fun onResponse(call: Call<Group>, response: Response<Group>) {
                    if (response.isSuccessful) {
                        Log.d("provera", "Received response: ${response.body()}")
                    } else {
                        // Obrada neuspešnog odgovora ako je potrebno
                    }
                }

                override fun onFailure(call: Call<Group>, t: Throwable) {
                    // Obrada greške u slučaju neuspelog zahteva
                }
            })
    }

    fun getShoppingItemsForUser(userId: String) {

        val jwtToken = tokenManager.getToken()

        apiService.getShoppingItemsForUser("Bearer $jwtToken", userId)
            .enqueue(object : Callback<List<ShoppingGroup>> {
                override fun onResponse(call: Call<List<ShoppingGroup>>, response: Response<List<ShoppingGroup>>) {
                    if (response.isSuccessful) {
                        _shoppingItems.value = response.body()
                        Log.d("Proba", "Received shopping items: ${response.body()}")
                    } else {
                        // Neuspela zahtev
                    }
                }

                override fun onFailure(call: Call<List<ShoppingGroup>>, t: Throwable) {
                    // Greška u mreži ili obradi zahteva
                }
            })
    }

    fun updateShoppingItem(itemId: String) {
        val jwtToken = tokenManager.getToken()

        apiService.updateShoppingItem("Bearer $jwtToken", itemId)
            .enqueue(object : Callback<ShoppingList> {
                override fun onResponse(call: Call<ShoppingList>, response: Response<ShoppingList>) {
                    if (response.isSuccessful) {
                        // Ažuriranje uspješno
                        Log.d("Proba", "Updated shopping item: ${response.body()}")
                        // Možete ažurirati lokalne podatke ako je potrebno
                    } else {
                        // Neuspješno ažuriranje
                        Log.e("Proba", "Failed to update shopping item: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<ShoppingList>, t: Throwable) {
                    // Greška u komunikaciji sa serverom
                    Log.e("Proba", "Failed to update shopping item: ${t.message}")
                }
            })
    }

}