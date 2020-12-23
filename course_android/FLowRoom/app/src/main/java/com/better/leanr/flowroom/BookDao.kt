package com.better.leanr.flowroom

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(bookBean: BookBean)

    @Query("select * from book")
    fun select(): Flow<List<BookBean>>

    @Delete
    fun delete(bookBean: BookBean)
}