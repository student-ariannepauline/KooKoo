package com.sunnyside.kookoo.student.model

import com.google.type.DateTime
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class AnnouncementModel(
    val author_name : String,
    val pic_link : String,
    val title: String,
    val body : String,
    val link: String,
    val deadline : LocalDate,
    val timestamp: LocalDateTime,
)