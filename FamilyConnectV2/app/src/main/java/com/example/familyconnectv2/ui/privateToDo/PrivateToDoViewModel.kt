package com.example.familyconnectv2.ui.privateToDo

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.familyconnectv2.models.CreateChatResponse
import com.example.familyconnectv2.models.ToDoRequest
import com.example.familyconnectv2.models.ToDoResponse
import com.example.familyconnectv2.models.ToDoTasksItem
import com.example.familyconnectv2.retrofit.RetrofitInstance
import com.example.familyconnectv2.sp.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class PrivateToDoViewModel : ViewModel() {

    private val taskService = RetrofitInstance.api

    private lateinit var tokenManager: TokenManager
    private val _tasks = MutableLiveData<List<ToDoTasksItem>>()
    val tasks: LiveData<List<ToDoTasksItem>>
        get() = _tasks


    fun initTokenManager(context: Context) {
        val sharedPreferences = context.getSharedPreferences("Token", Context.MODE_PRIVATE)
        tokenManager = TokenManager(sharedPreferences)
    }

    fun getAllTasks() {
        val jwtToken = tokenManager.getToken()

        taskService.getAllTasksForUser("Bearer $jwtToken").enqueue(object :
            Callback<List<ToDoTasksItem>> {
            override fun onResponse(
                call: Call<List<ToDoTasksItem>>,
                response: Response<List<ToDoTasksItem>>
            ) {
                if (response.isSuccessful) {
                    // Uspesan odgovor
                    _tasks.value = response.body()
                } else {
                    // Neuspesan odgovor
                    // Obradite grešku prema potrebi
                    val errorBody = response.errorBody()?.string()
                    Log.e("TasksViewModel", "Greška prilikom dohvatanja zadataka. Odgovor servera: $errorBody")
                }
            }

            override fun onFailure(call: Call<List<ToDoTasksItem>>, t: Throwable) {
                // Greška tokom poziva
                // Obradite grešku
                Log.e("TasksViewModel", "Greška prilikom slanja zahteva za dohvatanje zadataka", t)
            }
        })
    }

    fun deleteTask(taskId: String) {
        val jwtToken = tokenManager.getToken()

        taskService.deleteTask("Bearer $jwtToken", taskId).enqueue(object :
            Callback<CreateChatResponse> {
            override fun onResponse(
                call: Call<CreateChatResponse>,
                response: Response<CreateChatResponse>
            ) {
                if (response.isSuccessful) {
                    _tasks.value = _tasks.value?.filter { it._id != taskId }
                    Log.d("TasksViewModel", "Zadatak uspješno obrisan")
                    // Pozovite ponovo getAllTasks() ako trebate osvježiti listu zadataka
                } else {
                    // Neuspesan odgovor
                    // Obradite grešku prema potrebi
                    val errorBody = response.errorBody()?.string()
                    Log.e("TasksViewModel", "Greška prilikom brisanja zadatka. Odgovor servera: $errorBody")
                }
            }

            override fun onFailure(call: Call<CreateChatResponse>, t: Throwable) {
                // Greška tokom poziva
                // Obradite grešku
                Log.e("TasksViewModel", "Greška prilikom slanja zahteva za brisanje zadatka", t)
            }
        })
    }

}