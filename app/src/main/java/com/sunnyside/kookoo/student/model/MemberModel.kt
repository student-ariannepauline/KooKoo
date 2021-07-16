package com.sunnyside.kookoo.student.model

data class MemberModel (
    val uid: String = "",
    val name: String,
    val program: String = "",
    val level: Long = -1,
    val picLink : String
)