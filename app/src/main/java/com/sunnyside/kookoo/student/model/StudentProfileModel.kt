package com.sunnyside.kookoo.student.model

import java.util.*

data class StudentProfileModel(
    val uid: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val contactNumber: String = "",
    val address: String = "",
    val email: String = "",
    val birthDate: Date = Date(),
    val program: String = "",
    val level: Long = -1
)