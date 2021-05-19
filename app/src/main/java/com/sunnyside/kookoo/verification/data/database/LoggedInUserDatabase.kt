package com.sunnyside.kookoo.verification.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sunnyside.kookoo.verification.model.LoggedInUser

@Database(entities = [LoggedInUser::class], version = 1, exportSchema = false)
abstract class LoggedInUserDatabase : RoomDatabase() {
    abstract fun loggedInUserDao(): LoggedInUserDao

    companion object {
        @Volatile
        private var INSTANCE: LoggedInUserDatabase? = null

        fun getDatabase(context: Context): LoggedInUserDatabase {
            val tempInstance = INSTANCE

            if(tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LoggedInUserDatabase::class.java,
                    "logged_in_user_database"
                ).build()
                INSTANCE = instance

                return instance
            }
        }
    }
}