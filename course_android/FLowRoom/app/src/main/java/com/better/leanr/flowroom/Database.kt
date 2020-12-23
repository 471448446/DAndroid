package com.better.leanr.flowroom

import androidx.room.Room
import androidx.room.RoomDatabase

@androidx.room.Database(
    entities = [BookBean::class],
    version = 1,
    exportSchema = false
)
abstract class Database : RoomDatabase() {
    abstract fun bookDao(): BookDao

    companion object {
        val shared by lazy {
            Room.databaseBuilder(App.shared, Database::class.java, "book")
                .build()
        }
    }
}