package com.sunnyside.kookoo.student.model

import java.util.*

data class AnnouncementModel(
    val author_name : String,
    val pic_link : String,
    val class_name : String,
    val title: String,
    val body : String,
    val link: String,
    val deadline : Date
)