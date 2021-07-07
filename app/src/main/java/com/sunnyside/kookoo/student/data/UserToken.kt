package com.sunnyside.kookoo.student.data

object UserToken {
    var token: String = ""

    fun addToken(tokenToAdd: String) {
        token = tokenToAdd
    }
}