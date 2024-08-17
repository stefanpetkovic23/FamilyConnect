package com.example.familyconnectv2.ui.todo

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.familyconnectv2.models.ToDoGroupRequest
import com.example.familyconnectv2.models.ToDoGroupResponse
import com.example.familyconnectv2.retrofit.RetrofitInstance
import com.example.familyconnectv2.sp.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddToDoToGroupViewModel : ViewModel() {

    private val authService = RetrofitInstance.api

    private lateinit var tokenManager: TokenManager

    private val _taskAdded = MutableLiveData<Boolean>()
    val taskAdded: LiveData<Boolean>
        get() = _taskAdded

    fun initTokenManager(context: Context) {
        val sharedPreferences = context.getSharedPreferences("Token", Context.MODE_PRIVATE)
        tokenManager = TokenManager(sharedPreferences)
    }

    fun addTaskToGroup( request: ToDoGroupRequest) {
        val jwtToken = tokenManager.getToken()
        authService.addTaskToGroup("Bearer $jwtToken", request)
            .enqueue(object : Callback<ToDoGroupResponse> {
                override fun onResponse(call: Call<ToDoGroupResponse>, response: Response<ToDoGroupResponse>) {
                    if (response.isSuccessful) {
                        val todoGroupResponse = response.body()
                        _taskAdded.value=true
                        Log.d("Add ToDo to group", "Received response: $todoGroupResponse")
                    } else {

                    }
                }

                override fun onFailure(call: Call<ToDoGroupResponse>, t: Throwable) {
                    // Ovdje obradite grešku
                }
            })
    }
}