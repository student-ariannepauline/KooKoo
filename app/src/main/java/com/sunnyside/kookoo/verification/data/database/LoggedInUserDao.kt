package com.sunnyside.kookoo.verification.data.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sunnyside.kookoo.verification.model.LoggedInUser

@Dao
interface LoggedInUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun logIn(loggedInUser: LoggedInUser)

    @Query("SELECT * FROM logged_in_table LIMIT 1")
    suspend fun checkForUser(): LoggedInUser

    @Query("DELETE FROM logged_in_table")
    suspend fun logOut()
}