package com.sunnyside.kookoo.verification.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "logged_in_table")
data class LoggedInUser (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val email: String,
    val password: String
        )