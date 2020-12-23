package com.better.leanr.flowroom

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book")
data class BookBean(
    @PrimaryKey
    val id: Long,
    val Name: String
)