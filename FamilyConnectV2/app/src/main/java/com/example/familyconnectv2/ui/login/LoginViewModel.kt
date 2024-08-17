package com.example.familyconnectv2.ui.login

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.familyconnectv2.R
import com.example.familyconnectv2.interfaces.API
import com.example.familyconnectv2.models.LoginRequest
import com.example.familyconnectv2.models.userResponse
import com.example.familyconnectv2.retrofit.RetrofitInstance
import com.example.familyconnectv2.sp.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val authService = RetrofitInstance.api

    private lateinit var tokenManager: TokenManager

    private lateinit var sharedPreferences: SharedPreferences

    private val sharedPreferencesKey = "MySharedPreferences"
    private val loggedInKey = "loggedIn"

    fun saveLoginState(isLoggedIn: Boolean) {
        sharedPreferences.edit().putBoolean(loggedInKey, isLoggedIn).apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(loggedInKey, false)
    }

    private val _startDestination = MutableLiveData<Int>()
    val startDestination: LiveData<Int>
        get() = _startDestination

    fun initSharedPreferences(context: Context) {
        sharedPreferences = context.getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE)
        val rememberMe = isLoggedIn()
        Log.d("LoginViewModel", "Remember me: $rememberMe")
        if (rememberMe) {
            // Postavi vrednost LiveData kako bi obavestio fragment o potrebi za navigacijom
            _startDestination.value = R.id.nav_home
        }
        initTokenManager(context)
    }

    fun initTokenManager(context: Context) {
        val sharedPreferences = context.getSharedPreferences("Token", Context.MODE_PRIVATE)
        tokenManager = TokenManager(sharedPreferences)
    }

    fun loginUser(email: String, password: String, navController: NavController,rememberMe: Boolean) {
        val loginRequest = LoginRequest(email, password)

        authService.login(loginRequest).enqueue(object : Callback<userResponse> {
            override fun onResponse(call: Call<userResponse>, response: Response<userResponse>) {
                if (response.isSuccessful) {
                    // Pristup tokenu iz LoginResponse modela
                    val token = response.body()?.token
                    Log.d("LoginViewModel", "Uspesno logovanje, token: $token")
                    navController.navigate(R.id.action_loginFragment_to_nav_home)
                    if (rememberMe) {
                        saveLoginState(true)
                    }
                    token?.let {
                        tokenManager.saveToken(it)
                    }
                    val currentToken = tokenManager.getToken()
                    Log.d("LoginViewModel", "Trenutni token: $currentToken")
                } else {
                    // Neuspelo logovanje, pristupite greškama prema potrebi
                    val errorBody = response.errorBody()?.string()
                    Log.e("LoginViewModel", "Neuspelo logovanje. Odgovor servera: $errorBody")
                }
            }

            override fun onFailure(call: Call<userResponse>, t: Throwable) {
                Log.e("LoginViewModel", "Greška prilikom slanja zahteva za logovanje", t)
            }
        })
    }

}