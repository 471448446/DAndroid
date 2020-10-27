package com.better.learn.greendao.entity

import org.greenrobot.greendao.annotation.Entity
import org.greenrobot.greendao.annotation.Id

@Entity
class HappyBean(
    @Id(autoincrement = true)
    val id: Int,
    var name: String,
    var age: Int
)