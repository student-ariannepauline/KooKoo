package com.sunnyside.kookoo.student.model

import java.sql.Time
import java.util.*

data class ForecastModel(
    val author_name : String,
    val pic_link : String,
    val class_name : String,
    val title : String,
    val link : String,
    val meeting_date : Date,
    val meeting_time : Time
)