package com.better.learn.greendao

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.better.learn.greendao.entity.User
import com.better.learn.greendao.entity.daoSession
import kotlinx.android.synthetic.main.activity_main.*

/**
 * https://www.jianshu.com/p/df8bf52fe75d
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dao_insert.setOnClickListener {
            daoSession.userDao.insert(User("alex", "psd"))
            Toast.makeText(this, "insert ok", Toast.LENGTH_SHORT).show()
        }
    }
}