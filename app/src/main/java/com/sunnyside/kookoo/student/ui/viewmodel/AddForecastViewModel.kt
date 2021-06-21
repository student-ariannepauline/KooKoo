package com.sunnyside.kookoo.student.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sunnyside.kookoo.student.model.ForecastModel
import java.time.ZoneOffset
import java.util.*

class AddForecastViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Firebase.firestore

    fun addForecast(forecast: ForecastModel, classId: String) {
        val meetingTime = Date.from(forecast.meeting_time.toInstant(ZoneOffset.ofHours(8)))

        val newForecast = hashMapOf(
            "class_id" to classId,
            "title" to forecast.title,
            "link" to forecast.link,
            "status" to forecast.status,
            "meeting_time" to Timestamp(meetingTime)
        )

        db.collection("forecasts")
            .add(newForecast)
            .addOnSuccessListener { documentReference ->
                Log.d("tite", "Document with id: ${documentReference.id} successfully added")
            }
            .addOnFailureListener { e ->
                Log.w("tite", "Error adding document", e)
            }
    }
}