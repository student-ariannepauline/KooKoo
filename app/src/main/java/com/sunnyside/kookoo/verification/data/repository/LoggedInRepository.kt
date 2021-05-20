package com.sunnyside.kookoo.verification.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sunnyside.kookoo.verification.data.database.LoggedInUserDao
import com.sunnyside.kookoo.verification.model.LoggedInUser

class LoggedInRepository(private val loggedInUserDao: LoggedInUserDao) {

    suspend fun logIn(loggedInUser: LoggedInUser) {
        loggedInUserDao.logIn(loggedInUser)
    }

    suspend fun checkForUser(): LoggedInUser {
        return loggedInUserDao.checkForUser()
    }

    suspend fun logOut() {
        loggedInUserDao.logOut()
    }

}