package com.sunnyside.kookoo.student.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class JoinedClassModel(
    val class_id: String,
    val name: String,
    val is_admin: Boolean,
) : Parcelable