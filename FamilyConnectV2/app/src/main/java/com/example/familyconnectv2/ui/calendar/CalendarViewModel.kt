package com.example.familyconnectv2.ui.calendar

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.familyconnectv2.models.ActivitiesFromGroups
import com.example.familyconnectv2.models.CalendarActivity
import com.example.familyconnectv2.retrofit.RetrofitInstance
import com.example.familyconnectv2.sp.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalendarViewModel : ViewModel() {

    private val activityService = RetrofitInstance.api

    private lateinit var tokenManager: TokenManager

    // Define MutableLiveData for storing the result
    private val _activities = MutableLiveData<ActivitiesFromGroups>()
    val activities: LiveData<ActivitiesFromGroups>
        get() = _activities

    // Method for initializing TokenManager
    fun initTokenManager(context: Context) {
        val sharedPreferences = context.getSharedPreferences("Token", Context.MODE_PRIVATE)
        tokenManager = TokenManager(sharedPreferences)
    }

    fun getActivitiesForDate(userId: String) {
        val jwtToken = tokenManager.getToken()

        // Make the API call
        activityService.getActivitiesForDate("Bearer $jwtToken", userId)
            .enqueue(object : Callback<ActivitiesFromGroups> {
                override fun onResponse(call: Call<ActivitiesFromGroups>, response: Response<ActivitiesFromGroups>) {
                    if (response.isSuccessful) {
                        _activities.value = response.body()
                        Log.d("YourViewModel", "Received activities: ${response.body()}")
                    } else {
                        // Handle unsuccessful response
                    }
                }

                override fun onFailure(call: Call<ActivitiesFromGroups>, t: Throwable) {
                    // Handle network error or request processing failure
                }
            })
    }
}