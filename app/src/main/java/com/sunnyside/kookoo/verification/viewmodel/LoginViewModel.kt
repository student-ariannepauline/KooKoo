package com.sunnyside.kookoo.verification.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.sunnyside.kookoo.verification.data.database.LoggedInUserDatabase
import com.sunnyside.kookoo.verification.data.repository.LoggedInRepository
import com.sunnyside.kookoo.verification.model.LoggedInUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val loggedInRepository: LoggedInRepository
    val isLoggedIn: LiveData<List<LoggedInUser>>

    init {
        val loggedInUserDao = LoggedInUserDatabase.getDatabase(application).loggedInUserDao()
        loggedInRepository = LoggedInRepository(loggedInUserDao)
        isLoggedIn = loggedInRepository.isLoggedIn
    }

    fun login(loggedInUser: LoggedInUser) {
        viewModelScope.launch(Dispatchers.IO) {
            loggedInRepository.logIn(loggedInUser)
        }
    }

}