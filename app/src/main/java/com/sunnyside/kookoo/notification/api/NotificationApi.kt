package com.sunnyside.kookoo.notification.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NotificationApi {
    @GET("send")
    suspend fun notifyClass(
        @Query("token") token: String,
        @Query("topic") topic : String,
        @Query("title") title: String,
        @Query("body") body: String
        ) : Response<Message>
}