package com.sunnyside.kookoo.verification.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sunnyside.kookoo.verification.data.database.LoggedInUserDatabase
import com.sunnyside.kookoo.verification.data.repository.LoggedInRepository
import com.sunnyside.kookoo.verification.model.LoggedInUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val loggedInRepository: LoggedInRepository
    val loggedInUser: MutableLiveData<LoggedInUser> = MutableLiveData()

    init {
        val loggedInUserDao = LoggedInUserDatabase.getDatabase(application).loggedInUserDao()
        loggedInRepository = LoggedInRepository(loggedInUserDao)
    }

    fun login(loggedInUser: LoggedInUser) {
        viewModelScope.launch(Dispatchers.IO) {
            loggedInRepository.logIn(loggedInUser)
        }
    }

    fun checkForUser() {
        viewModelScope.launch {
            val user: LoggedInUser = loggedInRepository.checkForUser()
            loggedInUser.value = user
        }
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            loggedInRepository.logOut()
        }
    }

}