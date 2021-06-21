package com.sunnyside.kookoo.student.model

import java.sql.Time
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class ForecastModel(
    val title : String,
    val link : String,
    val status: String,
    val meeting_time : LocalDateTime
)