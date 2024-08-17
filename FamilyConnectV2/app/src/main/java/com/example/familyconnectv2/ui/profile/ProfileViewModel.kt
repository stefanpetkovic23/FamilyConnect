package com.example.familyconnectv2.ui.profile

import android.content.Context
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.familyconnectv2.R
import com.example.familyconnectv2.models.UserInfo
import com.example.familyconnectv2.retrofit.RetrofitInstance
import com.example.familyconnectv2.sp.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel : ViewModel() {
    private val authService = RetrofitInstance.api
    private lateinit var tokenManager: TokenManager

    private val _userData = MutableLiveData<UserInfo>()
    val userData: LiveData<UserInfo> get() = _userData

    fun initTokenManager(context: Context) {
        val sharedPreferences = context.getSharedPreferences("Token", Context.MODE_PRIVATE)
        tokenManager = TokenManager(sharedPreferences)
    }



    fun logoutUser(navController: NavController) {
        authService.logoutUser().enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    navController.navigate(R.id.action_profileFragment_to_splashFragment)
                    Log.d("LoginViewModel", "Odjava uspesna")
                } else {
                    Log.e("ProfileViewModel", "Neuspela odjava")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Greška prilikom slanja zahteva za odjavljivanje
            }
        })
    }


    fun doSomethingWithToken() {
        val token = tokenManager.getToken()
        if (token != null) {
            // Radite šta želite sa tokenom
            Log.d("ProfileViewModel", "Token: $token")
        } else {
            // Token nije dostupan, možete preduzeti odgovarajuće akcije
            Log.e("ProfileViewModel", "Token nije dostupan")
        }
    }

    fun getUserData(email: String) {
        val token = tokenManager.getToken()

            authService.getUserData("Bearer $token", email).enqueue(object : Callback<UserInfo> {
                override fun onResponse(call: Call<UserInfo>, response: Response<UserInfo>) {
                    if (response.isSuccessful) {
                        val userInfo = response.body()
                        _userData.value = userInfo
                        // Ovde radite šta želite sa podacima o korisniku
                        Log.d("ProfileViewModel", "Podaci o korisniku: $userInfo")
                    } else {
                        Log.e("ProfileViewModel", "Neuspeli zahtev za podacima o korisniku")
                    }
                }

                override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                    Log.e("ProfileViewModel", "Greška prilikom slanja zahteva za podacima o korisniku", t)
                }
            })

    }

}