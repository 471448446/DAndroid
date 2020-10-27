package com.better.learn.greendao.entity

import com.better.learn.greendao.App
import com.better.learn.greendao.gen.DaoMaster
import com.better.learn.greendao.gen.DaoSession

val daoSession: DaoSession by lazy {
    val hel = DaoMaster.DevOpenHelper(App.shared, "name")
    DaoMaster(hel.writableDb).newSession()
}