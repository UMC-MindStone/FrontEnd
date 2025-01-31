package com.example.mindstone.ui.auth.signup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignupViewModel(application: Application) : AndroidViewModel(application) {

    private val _email = MutableLiveData<String>("")
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>("")
    val password: LiveData<String> = _password

    private val _nickname = MutableLiveData<String>("")
    val nickname: LiveData<String> = _nickname

    private val _mbti = MutableLiveData<String>("")
    val mbti: LiveData<String> = _mbti

    private val _birthday = MutableLiveData<String>("")
    val birthday: LiveData<String> = _birthday

    private val _job = MutableLiveData<String>("")
    val job: LiveData<String> = _job

    private val _shareScope = MutableLiveData<Boolean>(false)
    val shareScope: LiveData<Boolean> = _shareScope

    private val _marketingAgree = MutableLiveData<Boolean>(false)
    val marketingAgree: LiveData<Boolean> = _marketingAgree

    private val _role = MutableLiveData<String>("")
    val role: LiveData<String> = _role

    // 값 업데이트 메서드 추가
    fun updateEmail(value: String) {
        _email.value = value
    }

    fun updatePassword(value: String) {
        _password.value = value
    }

    fun updateNickname(value: String) {
        _nickname.value = value
    }

    fun updateMbti(value: String) {
        _mbti.value = value
    }

    fun updateBirthday(value: String) {
        _birthday.value = value
    }

    fun updateJob(value: String) {
        _job.value = value
    }

    fun updateShareScope(value: Boolean) {
        _shareScope.value = value
    }

    fun updateMarketingAgree(value: Boolean) {
        _marketingAgree.value = value
    }

    fun updateRole(value: String) {
        _role.value = value
    }
}

