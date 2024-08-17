package com.example.familyconnectv2.ui.register

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.familyconnectv2.R
import com.example.familyconnectv2.interfaces.API
import com.example.familyconnectv2.models.User
import com.example.familyconnectv2.models.userResponse
import com.example.familyconnectv2.retrofit.RetrofitInstance
import com.example.familyconnectv2.retrofit.RetrofitInstance.api
import com.example.familyconnectv2.sp.TokenManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterViewModel : ViewModel() {

    private val api: API = RetrofitInstance.api
    private lateinit var tokenManager: TokenManager

    private var password: String = ""
    private var confirmPassword: String = ""

    fun getToken(): String? {
        return tokenManager.getToken()
    }

    fun setPasswords(password: String, confirmPassword: String) {
        this.password = password
        this.confirmPassword = confirmPassword
    }

    fun initTokenManager(context: Context) {
        val sharedPreferences = context.getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)
        tokenManager = TokenManager(sharedPreferences)
    }


    fun registerUser(name: String, email: String, password: String , confirmPassword: String,navController: NavController) {



        if (password == confirmPassword){
            val user = User(name, email, password);

            api.registerUser(user).enqueue(object : Callback<userResponse> {
                override fun onResponse(call: Call<userResponse>, response: Response<userResponse>) {
                    if (response.isSuccessful) {
                        // Pristup tokenu iz userResponse modela
                        val token = response.body()?.token
                        Log.d("RegisterViewModel", "Registracija uspešna, token: $token")
                        token?.let {
                            tokenManager.saveToken(it)
                        }
                        navController.navigate(R.id.action_registerFragment_to_loginFragment)
                        val currentToken = getToken()
                        Log.d("RegisterViewModel", "Trenutni token: $currentToken")
                    } else {
                        // Neuspela registracija, pristupite greškama prema potrebi
                        val errorBody = response.errorBody()?.string()
                        Log.e("RegisterViewModel", "Neuspela registracija. Odgovor servera: $errorBody")
                    }
                }

                override fun onFailure(call: Call<userResponse>, t: Throwable) {
                    Log.e("RegisterViewModel", "Greška prilikom slanja zahteva za registraciju", t)
                }
            })
        }
        else{
                Log.e("RegisterViewModel", "Sifre se ne poklapaju")
        }

    }


}







