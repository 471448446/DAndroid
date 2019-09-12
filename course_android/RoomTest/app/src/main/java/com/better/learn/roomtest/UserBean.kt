package com.better.learn.roomtest

import androidx.room.Entity
import androidx.room.PrimaryKey

open class UserBean(
    @PrimaryKey
    val name: String
)

@Entity(tableName = "userA")
class UserBeanA(name: String) : UserBean(name)

@Entity(tableName = "userB")
class UserBeanB(name: String) : UserBean(name)