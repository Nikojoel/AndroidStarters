package com.example.helloworld2

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [(User::class), (ContactInfo::class)], version = 1)
abstract class UserDB: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun contactDao(): ContactInfoDao

    companion object {
        private var syncInstance: UserDB? = null

        @Synchronized
        fun get(context: Context): UserDB {
            if (syncInstance == null) {
                syncInstance = Room.databaseBuilder(context.applicationContext, UserDB::class.java, "user.db").build()
            }
            return syncInstance!!
        }
    }
}