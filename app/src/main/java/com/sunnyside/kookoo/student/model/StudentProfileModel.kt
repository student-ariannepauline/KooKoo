package com.sunnyside.kookoo.student.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class StudentProfileModel(
    val uid: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val contactNumber: String = "",
    val address: String = "",
    val email: String = "",
    val birthDate: Date = Date(),
    val program: String = "",
    val level: Long = -1,
    val picLink : String
) : Parcelable