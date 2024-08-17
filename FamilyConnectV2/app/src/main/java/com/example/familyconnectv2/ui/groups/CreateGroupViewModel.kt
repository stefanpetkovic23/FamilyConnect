package com.example.familyconnectv2.ui.groups

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import com.example.familyconnectv2.models.CreateChatResponse
import com.example.familyconnectv2.models.Group
import com.example.familyconnectv2.models.GroupCreationRequest
import com.example.familyconnectv2.models.UserIdResponse
import com.example.familyconnectv2.retrofit.RetrofitInstance
import com.example.familyconnectv2.sp.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateGroupViewModel(private val savedStateHandle: SavedStateHandle): ViewModel() {
    private val userService = RetrofitInstance.api
    private val _userId = MutableLiveData<String>()
    val userId: LiveData<String> = _userId
    private lateinit var tokenManager: TokenManager

    private val _groupCreated = MutableLiveData<Boolean>()
    val groupCreated: LiveData<Boolean> get() = _groupCreated

    fun initTokenManager(context: Context) {
        val sharedPreferences = context.getSharedPreferences("Token", Context.MODE_PRIVATE)
        tokenManager = TokenManager(sharedPreferences)
    }

    fun getUserIdByEmail(email: String) {
        val jwtToken = tokenManager.getToken()

        userService.getUserIdByEmail("Bearer $jwtToken", email).enqueue(object :
            Callback<UserIdResponse> {
            override fun onResponse(call: Call<UserIdResponse>, response: Response<UserIdResponse>) {
                if (response.isSuccessful) {
                    // Uspesan odgovor
                    val userIdResponse = response.body()
                    _userId.value = userIdResponse?.userId
                    Log.d("UsersAdapter", "Binding user: $userIdResponse")
                } else {
                    // Neuspesan odgovor
                    // Obradite grešku prema potrebi
                    val errorBody = response.errorBody()?.string()
                    Log.e("YourViewModel", "Greška prilikom dohvatanja korisničkog ID-a. Odgovor servera: $errorBody")
                }
            }

            override fun onFailure(call: Call<UserIdResponse>, t: Throwable) {
                // Greška tokom poziva
                // Obradite grešku
                Log.e("YourViewModel", "Greška prilikom slanja zahteva za dohvatanje korisničkog ID-a", t)
            }
        })
    }

    fun createGroup(name: String, users: List<String>) {
        // Dobavite JWT token iz vašeg sistema autentifikacije
        val jwtToken = tokenManager.getToken() // Pretpostavljam da tokenManager čuva token

        val groupRequest = GroupCreationRequest(name, users)

        userService.createGroup("Bearer $jwtToken", groupRequest).enqueue(object :
            Callback<Group> {
            override fun onResponse(call: Call<Group>, response: Response<Group>) {
                if (response.isSuccessful) {
                    // Uspesan odgovor
                    val groupResponse = response.body()
                    Log.d("GroupViewModel", "Grupa uspešno kreirana. Odgovor servera: $groupResponse")
                    _groupCreated.value = true
                } else {
                    // Neuspesan odgovor
                    // Obradite grešku prema potrebi
                    val errorBody = response.errorBody()?.string()
                    Log.e("GroupViewModel", "Greška prilikom kreiranja grupe. Odgovor servera: $errorBody")
                }
            }

            override fun onFailure(call: Call<Group>, t: Throwable) {
                // Greška tokom poziva
                // Obradite grešku
                Log.e("GroupViewModel", "Greška prilikom slanja zahteva za kreiranje grupe", t)
            }
        })
    }

    fun createGroupChat(name: String, users: List<String>) {

        val jwtToken = tokenManager.getToken() // Pretpostavljam da tokenManager čuva token
        val groupRequest = GroupCreationRequest(name, users)


        userService.createGroupChat("Bearer $jwtToken", groupRequest).enqueue(object : Callback<CreateChatResponse> {
            override fun onResponse(call: Call<CreateChatResponse>, response: Response<CreateChatResponse>) {
                if (response.isSuccessful) {
                    // Uspesan odgovor
                    val createChatResponse = response.body()
                    Log.d("CreateGroupViewModel", "Grupa uspešno kreirana. Odgovor servera: $createChatResponse")
                    // Ovde možete izvršiti odgovarajuće akcije nakon uspešnog kreiranja grupe
                } else {
                    // Neuspesan odgovor
                    // Obradite grešku prema potrebi
                    val errorBody = response.errorBody()?.string()
                    Log.e("CreateGroupViewModel", "Greška prilikom kreiranja grupe. Odgovor servera: $errorBody")
                    // Ovde možete obraditi grešku
                }
            }

            override fun onFailure(call: Call<CreateChatResponse>, t: Throwable) {
                // Greška tokom poziva
                // Obradite grešku
                Log.e("CreateGroupViewModel", "Greška prilikom slanja zahteva za kreiranje grupe", t)
                // Ovde možete obraditi grešku
            }
        })
    }

}