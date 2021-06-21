package com.sunnyside.kookoo.student.data

import com.sunnyside.kookoo.student.model.JoinedClassModel

object JoinedClass {
    var joinedClass : JoinedClassModel = JoinedClassModel(" ", " ", false)

    fun enterClass(classToEnter : JoinedClassModel) {
        joinedClass = classToEnter
    }
}