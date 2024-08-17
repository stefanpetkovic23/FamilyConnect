package com.example.familyconnectv2.ui.addtask

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.familyconnectv2.models.ToDoRequest
import com.example.familyconnectv2.models.ToDoResponse
import com.example.familyconnectv2.retrofit.RetrofitInstance
import com.example.familyconnectv2.sp.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date

class TaskViewModel: ViewModel() {

    private val authService = RetrofitInstance.api

    private lateinit var tokenManager: TokenManager

    fun initTokenManager(context: Context) {
        val sharedPreferences = context.getSharedPreferences("Token", Context.MODE_PRIVATE)
        tokenManager = TokenManager(sharedPreferences)
    }

    fun addTask(title: String, description: String, dueDate: Date,) {
        val taskRequest = ToDoRequest(description, dueDate,title)

        // Dobavite JWT token iz vašeg sistema autentifikacije
        val jwtToken = tokenManager.getToken() // Pretpostavljam da tokenManager čuva token

        authService.addTask("Bearer $jwtToken", taskRequest).enqueue(object :
            Callback<ToDoResponse> {
            override fun onResponse(call: Call<ToDoResponse>, response: Response<ToDoResponse>) {
                if (response.isSuccessful) {
                    // Uspesan odgovor
                    val taskResponse = response.body()
                    Log.d("TaskViewModel", "Zadatak uspešno dodat. Odgovor servera: $taskResponse")
                    // Dodatne akcije prema potrebi (npr. osvežavanje prikaza zadatka)
                } else {
                    // Neuspesan odgovor
                    // Obradite grešku prema potrebi
                    val errorBody = response.errorBody()?.string()
                    Log.e("TaskViewModel", "Greška prilikom dodavanja zadatka. Odgovor servera: $errorBody")
                }
            }

            override fun onFailure(call: Call<ToDoResponse>, t: Throwable) {
                // Greška tokom poziva
                // Obradite grešku
                Log.e("TaskViewModel", "Greška prilikom slanja zahteva za dodavanje zadatka", t)
            }
        })
    }

}