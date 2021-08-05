package com.better.learn.jetpack.app_startup

import android.content.Context
import android.widget.Toast
import androidx.startup.Initializer

/*
1. 再manifest中配置
2. 代码中手动初始化
 */

class InitializesAge : Initializer<Age> {
    override fun create(context: Context): Age {
        return Age(12)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        // 前置依赖
        // 初始化Age的时候，需要先初始化Name
        return listOf(InitializesName::class.java)
    }
}

class InitializesName : Initializer<Name> {
    override fun create(context: Context): Name = Name("zhangsan")

    override fun dependencies(): List<Class<out Initializer<*>>> {
        // 什么都不依赖
        return emptyList()
    }
}

class InitializesMan : Initializer<Man> {
    override fun create(context: Context): Man = Man("zhangsan")

    override fun dependencies(): List<Class<out Initializer<*>>> {
        // 什么都不依赖
        return listOf(InitializesWoman::class.java)
    }
}

class InitializesWoman : Initializer<Woman> {
    override fun create(context: Context): Woman = Woman("lisi").also {
        Toast.makeText(context, "init done", Toast.LENGTH_SHORT).show()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        // 什么都不依赖
        return emptyList()
    }
}