package com.sunnyside.kookoo.student.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.sql.Time
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Parcelize
data class ForecastModel(
    val documentID: String,
    val title : String,
    val link : String,
    val status: String,
    val meeting_time : LocalDateTime
) : Parcelable