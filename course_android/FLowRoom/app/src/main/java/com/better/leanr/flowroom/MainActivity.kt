package com.better.leanr.flowroom

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 文中建议在Repository处使用Flow
 * https://medium.com/better-programming/no-more-livedata-in-repositories-in-kotlin-85f5a234a8fe
 * asLiveData()
 * https://codelabs.developers.google.com/codelabs/advanced-kotlin-coroutines/?hl=he#2a
 */
class MainActivity : AppCompatActivity() {
    private val mainModule = MainModule()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        add_book.setOnClickListener {
            mainModule.addBook()
        }
        delete_book.setOnClickListener {
            mainModule.deleteBook()
        }
        mainModule.booksLiveData1.observe(this@MainActivity) {
            book_list.text = it.joinToString("\n")
        }
    }
}