package com.better.learn.roomtest

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserBeanA::class, UserBeanB::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getUserDao(): UserDao

    companion object {
        val shared by lazy {
            Room.databaseBuilder(App.shared, AppDatabase::class.java, "room").build()
        }
    }
}