package com.example.familyconnectv2.ui.addactivity

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.familyconnectv2.models.ActivityDetails
import com.example.familyconnectv2.models.ActivityFull
import com.example.familyconnectv2.models.AddActivityGroupResponse
import com.example.familyconnectv2.models.AddActivityToGroup
import com.example.familyconnectv2.retrofit.RetrofitInstance
import com.example.familyconnectv2.sp.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddActivityViewModel : ViewModel() {

    private val authService = RetrofitInstance.api

    private lateinit var tokenManager: TokenManager

    private val _activityAdded = MutableLiveData<Boolean>()
    val activityAdded: LiveData<Boolean>
        get() = _activityAdded

    fun initTokenManager(context: Context) {
        val sharedPreferences = context.getSharedPreferences("Token", Context.MODE_PRIVATE)
        tokenManager = TokenManager(sharedPreferences)
    }

    fun addActivityToGroupIfVisible(groupId: String, activityDetails: ActivityDetails) {
        // Pozovite prvu metodu kada je spinner vidljiv
        addActivityToGroup(groupId, activityDetails)
    }

    fun addActivityIfInvisible(token: String, activityDetails: ActivityDetails) {
        // Pozovite drugu metodu kada spinner nije vidljiv
        addActivity(activityDetails)
    }

    fun addActivityToGroup(groupId: String, activityDetails: ActivityDetails) {
        val request = AddActivityToGroup(groupId, activityDetails)

        // Dobavite JWT token iz vašeg sistema autentifikacije
        val jwtToken = tokenManager.getToken() // Pretpostavljam da tokenManager čuva token

        authService.addActivityToGroup("Bearer $jwtToken", request).enqueue(object :
            Callback<AddActivityGroupResponse> {
            override fun onResponse(call: Call<AddActivityGroupResponse>, response: Response<AddActivityGroupResponse>) {
                if (response.isSuccessful) {
                    // Uspesan odgovor
                    val addActivityResponse = response.body()
                    Log.d("TaskViewModel", "Aktivnost uspešno dodata u grupu. Odgovor servera: $addActivityResponse")
                    _activityAdded.value = true

                } else {
                    // Neuspesan odgovor

                    val errorBody = response.errorBody()?.string()
                    Log.e("TaskViewModel", "Greška prilikom dodavanja aktivnosti u grupu. Odgovor servera: $errorBody")
                }
            }

            override fun onFailure(call: Call<AddActivityGroupResponse>, t: Throwable) {
                // Greška tokom poziva
                // Obradite grešku
                Log.e("TaskViewModel", "Greška prilikom slanja zahteva za dodavanje aktivnosti u grupu", t)
            }
        })
    }

    fun addActivity(activityDetails: ActivityDetails) {

        val jwtToken = tokenManager.getToken()
        authService.addActivity("Bearer $jwtToken", activityDetails).enqueue(object :
            Callback<ActivityFull> {
            override fun onResponse(call: Call<ActivityFull>, response: Response<ActivityFull>) {
                if (response.isSuccessful) {
                    // Uspesan odgovor
                    val activity = response.body()
                    Log.d("ActivityViewModel", "Aktivnost uspešno dodata ali ne u grupu. Odgovor servera: $activity")
                    _activityAdded.value = true
                    // Dodatne akcije prema potrebi
                } else {
                    // Neuspesan odgovor
                    // Obradite grešku prema potrebi
                    val errorBody = response.errorBody()?.string()
                    Log.e("ActivityViewModel", "Greška prilikom dodavanja aktivnosti. Odgovor servera: $errorBody")
                }
            }

            override fun onFailure(call: Call<ActivityFull>, t: Throwable) {
                // Greška tokom poziva
                // Obradite grešku
                Log.e("ActivityViewModel", "Greška prilikom slanja zahteva za dodavanje aktivnosti", t)
            }
        })
    }

}