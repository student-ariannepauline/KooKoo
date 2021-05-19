package com.sunnyside.kookoo.verification.data.repository

import androidx.lifecycle.LiveData
import com.sunnyside.kookoo.verification.data.database.LoggedInUserDao
import com.sunnyside.kookoo.verification.model.LoggedInUser

class LoggedInRepository(private val loggedInUserDao: LoggedInUserDao) {
    val isLoggedIn: LiveData<List<LoggedInUser>> = loggedInUserDao.loggedIn()

    suspend fun logIn(loggedInUser: LoggedInUser) {
        loggedInUserDao.logIn(loggedInUser)
    }

}