package com.sunnyside.kookoo.student.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sunnyside.kookoo.notification.api.NotificationSender
import com.sunnyside.kookoo.notification.api.RetrofitInstance
import com.sunnyside.kookoo.student.data.JoinedClass
import com.sunnyside.kookoo.student.data.UserToken
import com.sunnyside.kookoo.student.model.AnnouncementModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class AddPostViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Firebase.firestore

    fun addPost(announcement: AnnouncementModel, classId: String) {
        val date = Date.from(announcement.deadline.atStartOfDay().toInstant(ZoneOffset.UTC))
        val formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy")
        val dateId = announcement.deadline.format(formatter)

        val userToken = UserToken.token

        NotificationSender.sendNotificationToClass(userToken, classId,"New Announcement! in ${JoinedClass.joinedClass.name}", announcement.title )

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

                val event = mapOf(
                    "type" to "announcement",
                    "data" to mapOf(
                        "id" to documentReference.id,
                        "title" to announcement.title,
                    )
                )

                val eventsRef = db.collection("events")
                    .whereEqualTo("class_id", classId)
                    .whereEqualTo("date", dateId)

                eventsRef
                    .get()
                    .addOnSuccessListener { snapshot ->
                        if (!snapshot.isEmpty) {
                            val documentEvent = snapshot.first()
                            db.collection("events").document(documentEvent.id).update(
                                "events", FieldValue.arrayUnion(event)
                            )
                        } else {
                            val data = hashMapOf(
                                "class_id" to classId,
                                "date" to dateId,
                                "events" to arrayListOf(event)
                            )

                            db.collection("events").add(data)
                        }
                    }
            }
            .addOnFailureListener { e ->
                Log.w("tite", "Error adding document", e)
            }
    }
}