package com.sunnyside.kookoo.student.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sunnyside.kookoo.student.model.AnnouncementModel
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*

class AddPostViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Firebase.firestore

    fun addPost(announcement: AnnouncementModel, classId: String) {
        val date = Date.from(announcement.deadline.atStartOfDay().toInstant(ZoneOffset.UTC))
        val newPost = hashMapOf(
            "class_id" to classId,
            "author_name" to announcement.author_name,
            "pic_link" to announcement.pic_link,
            "title" to announcement.title,
            "body" to announcement.body,
            "link" to announcement.link,
            "deadline" to Timestamp(date),
            "timestamp" to Timestamp.now()
        )

        db.collection("announcements")
            .add(newPost)
            .addOnSuccessListener { documentReference ->
                Log.d("tite", "Document with id: ${documentReference.id} successfully added")
            }
            .addOnFailureListener { e ->
                Log.w("tite", "Error adding document", e)
            }
    }
}