package com.example.familyconnectv2.sp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {
    private val _userEmail = MutableLiveData<String>()
    val userEmail: LiveData<String> get() = _userEmail

    fun setUserEmail(email: String) {
        _userEmail.value = email
    }
}