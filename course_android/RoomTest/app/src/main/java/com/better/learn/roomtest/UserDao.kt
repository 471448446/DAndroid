package com.better.learn.roomtest

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(user: UserBeanA)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(user: UserBeanB)

    @Query("select * from userA")
    fun getUsersA(): List<UserBean>

    @Query("select * from userB")
    fun getUsersB(): List<UserBean>
}