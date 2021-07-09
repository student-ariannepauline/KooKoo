package com.sunnyside.kookoo.notification.api

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

object NotificationSender {

    fun sendNotificationToClass(userToken : String, classId : String, title : String, body : String) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = RetrofitInstance.api.notifyClass(userToken, classId, title, body)

                if (response.isSuccessful) {
                    val message = response.body()

                    if (message != null) {
                        Log.d("tite", "sent by ${message.message["sent by"]}")
                    }
                }
            } catch (e : SocketTimeoutException) {
                Log.w("tite", "Can't connect to server.", e)
            }
        }
        Log.d("tite", title)
    }

}