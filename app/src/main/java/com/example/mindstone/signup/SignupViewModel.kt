package com.example.mindstone.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignupViewModel : ViewModel() {
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val nickname = MutableLiveData<String>()
    val mbti = MutableLiveData<String>()
    val birthday = MutableLiveData<String>()
    val job = MutableLiveData<String>()
    val shareScope = MutableLiveData<Boolean>()
    val marketingAgree = MutableLiveData<Boolean>()
    val role = MutableLiveData<String>()



}