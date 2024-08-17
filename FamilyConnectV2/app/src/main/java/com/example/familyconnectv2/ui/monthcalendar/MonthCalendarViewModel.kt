package com.example.familyconnectv2.ui.monthcalendar

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.familyconnectv2.models.ActivityFull
import com.example.familyconnectv2.models.CreateChatResponse
import com.example.familyconnectv2.retrofit.RetrofitInstance
import com.example.familyconnectv2.sp.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MonthCalendarViewModel : ViewModel() {
    private val _showSpinner = MutableLiveData<Boolean>()
    val showSpinner: LiveData<Boolean> get() = _showSpinner

    fun setShowSpinner(value: Boolean) {
        _showSpinner.value = value
        Log.d("MonthCalendarViewModel", "showSpinner set to: $value")
    }

    private val activityService = RetrofitInstance.api

    private lateinit var tokenManager: TokenManager

    private val _activities = MutableLiveData<List<ActivityFull>>()
    val activities: LiveData<List<ActivityFull>>
        get() = _activities

    fun initTokenManager(context: Context) {
        val sharedPreferences = context.getSharedPreferences("Token", Context.MODE_PRIVATE)
        tokenManager = TokenManager(sharedPreferences)
    }

    fun fetchActivities( userId: String) {
        val jwtToken = tokenManager.getToken()
        activityService.getAllActivitiesByUser("Bearer $jwtToken", userId)
            .enqueue(object : Callback<List<ActivityFull>> {
                override fun onResponse(call: Call<List<ActivityFull>>, response: Response<List<ActivityFull>>) {
                    if (response.isSuccessful) {
                        _activities.value = response.body()
                        Log.d("ActivityViewModel", "Received response: ${response.body()}")
                    } else {
                        Log.e("ActivityViewModel", "Failed to fetch activities: ${response.errorBody()}")
                    }
                }

                override fun onFailure(call: Call<List<ActivityFull>>, t: Throwable) {
                    Log.e("ActivityViewModel", "Error fetching activities: ${t.message}")
                }
            })
    }

    fun deleteActivity(activityId: String) {
        val jwtToken = tokenManager.getToken()
        activityService.deleteActivity("Bearer $jwtToken", activityId)
            .enqueue(object : Callback<CreateChatResponse> {
                override fun onResponse(call: Call<CreateChatResponse>, response: Response<CreateChatResponse>) {
                    if (response.isSuccessful) {
                        // Aktivnost je uspješno obrisana
                        _activities.value = _activities.value?.filter { it._id != activityId }
                        Log.d("MonthCalendarViewModel", "Activity deleted successfully")
                    } else {
                        // Došlo je do greške pri brisanju aktivnosti
                        Log.e("MonthCalendarViewModel", "Failed to delete activity: ${response.errorBody()}")
                    }
                }

                override fun onFailure(call: Call<CreateChatResponse>, t: Throwable) {
                    // Greška pri komunikaciji sa serverom
                    Log.e("MonthCalendarViewModel", "Error deleting activity: ${t.message}")
                }
            })
    }

}