package com.example.familyconnectv2.ui.groups

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.familyconnectv2.models.Group
import com.example.familyconnectv2.models.groupResponse
import com.example.familyconnectv2.retrofit.RetrofitInstance
import com.example.familyconnectv2.sp.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GalleryViewModel : ViewModel() {

    private val groupService = RetrofitInstance.api
    private lateinit var tokenManager: TokenManager

    // Ažurirajte tip LiveData-a da biste koristili novi tip odgovora
    private val _groups = MutableLiveData<List<Group>>()
    val groups: LiveData<List<Group>> = _groups

    fun initTokenManager(context: Context) {
        val sharedPreferences = context.getSharedPreferences("Token", Context.MODE_PRIVATE)
        tokenManager = TokenManager(sharedPreferences)
    }

    // Ažurirajte funkciju za dobijanje grupa
    fun getGroupsByUserId(userId: String) {
        val jwtToken = tokenManager.getToken()

        groupService.getGroupsByUserId("Bearer $jwtToken", userId)
            .enqueue(object : Callback<groupResponse> {  // Ažurirajte tip odgovora
                override fun onResponse(
                    call: Call<groupResponse>,  // Ažurirajte tip odgovora
                    response: Response<groupResponse>  // Ažurirajte tip odgovora
                ) {
                    if (response.isSuccessful) {
                        // Uspesan odgovor
                        val groupsResponse = response.body()  // Ažurirajte na novi tip odgovora
                        _groups.value = groupsResponse?.groups  // Pristupite listi grupa u odgovoru
                    } else {
                        // Neuspesan odgovor
                        val errorBody = response.errorBody()?.string()
                        Log.e("GalleryViewModel", "Greška prilikom dohvatanja grupa. Odgovor servera: $errorBody")
                    }
                }

                override fun onFailure(call: Call<groupResponse>, t: Throwable) {
                    // Greška tokom poziva
                    // Obradite grešku
                    Log.e("GalleryViewModel", "Greška prilikom slanja zahteva za dohvatanje grupa", t)
                }
            })
    }
}
