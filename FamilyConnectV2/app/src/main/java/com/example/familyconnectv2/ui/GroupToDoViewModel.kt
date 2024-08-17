package com.example.familyconnectv2.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.familyconnectv2.models.Task
import com.example.familyconnectv2.models.TasksFromGroup
import com.example.familyconnectv2.models.ToDoTasksItem
import com.example.familyconnectv2.retrofit.RetrofitInstance
import com.example.familyconnectv2.sp.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupToDoViewModel : ViewModel() {

    private val grouptaskService = RetrofitInstance.api

    private lateinit var tokenManager: TokenManager
    private val _tasks = MutableLiveData<TasksFromGroup>()
    val tasks: LiveData<TasksFromGroup>
        get() = _tasks

    fun initTokenManager(context: Context) {
        val sharedPreferences = context.getSharedPreferences("Token", Context.MODE_PRIVATE)
        tokenManager = TokenManager(sharedPreferences)
    }

    fun fetchTasks(userId: String) {
        // Dobavite JWT token iz vašeg sistema autentifikacije
        val jwtToken = tokenManager.getToken() // Pretpostavljam da tokenManager čuva token

        grouptaskService.getTasksFromGroupsByUserId("Bearer $jwtToken", userId)
            .enqueue(object : Callback<TasksFromGroup> {
                override fun onResponse(call: Call<TasksFromGroup>, response: Response<TasksFromGroup>) {
                    if (response.isSuccessful) {
                        _tasks.value = response.body()
                        Log.d("GroupToDoFragment", "Received response: ${response.body()}")
                    } else {

                    }
                }

                override fun onFailure(call: Call<TasksFromGroup>, t: Throwable) {

                }
            })
    }

    fun updateTask(taskId: String) {
        // Dobavite JWT token iz vašeg sistema autentifikacije
        val jwtToken = tokenManager.getToken() // Pretpostavljam da tokenManager čuva token

        grouptaskService.updateTask("Bearer $jwtToken", taskId)
            .enqueue(object : Callback<Task> { // Promenite ime klase ako je drugačije
                override fun onResponse(call: Call<Task>, response: Response<Task>) {
                    if (response.isSuccessful) {
                        val updatedTask = response.body()
                        var error = response.errorBody()
                        Log.d("GroupToDoViewModel", "Task updated successfully: $updatedTask")
                        // Implementirajte logiku koja je potrebna nakon uspešnog ažuriranja zadatka
                    } else {
                        Log.e("GroupToDoViewModel", "Failed to update task: ${response.errorBody()}")
                        // Implementirajte logiku koja se izvršava ako ažuriranje nije uspelo
                    }
                }

                override fun onFailure(call: Call<Task>, t: Throwable) {
                    Log.e("GroupToDoViewModel", "Error updating task: ${t.message}")
                    // Implementirajte logiku koja se izvršava u slučaju greške prilikom ažuriranja zadatka
                }
            })
    }

    fun updatePrivateTodo(taskId: String) {
        // Dobavite JWT token iz vašeg sistema autentifikacije
        val jwtToken = tokenManager.getToken() // Pretpostavljam da tokenManager čuva token

        grouptaskService.updatePrivateTask("Bearer $jwtToken", taskId)
            .enqueue(object : Callback<Task> { // Promenite ime klase ako je drugačije
                override fun onResponse(call: Call<Task>, response: Response<Task>) {
                    if (response.isSuccessful) {
                        val updatedTask = response.body()
                        var error = response.errorBody()
                        Log.d("GroupToDoViewModel", "Task updated successfully: $updatedTask")
                        // Implementirajte logiku koja je potrebna nakon uspešnog ažuriranja zadatka
                    } else {
                        Log.e("GroupToDoViewModel", "Failed to update task: ${response.errorBody()}")
                        // Implementirajte logiku koja se izvršava ako ažuriranje nije uspelo
                    }
                }

                override fun onFailure(call: Call<Task>, t: Throwable) {
                    Log.e("GroupToDoViewModel", "Error updating task: ${t.message}")
                    // Implementirajte logiku koja se izvršava u slučaju greške prilikom ažuriranja zadatka
                }
            })
    }

}