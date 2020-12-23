package com.better.leanr.flowroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainModule : ViewModel() {
    private val repository = BookRepository()

    // way1
    val booksLiveData1 = repository.books().asLiveData()
    // way2
    // val booksLiveData2: MutableLiveData<List<BookBean>> = MutableLiveData()

    init {
//        viewModelScope.launch {
//            repository.books().collect {
//                booksLiveData2.value = it
//            }
//        }

    }

    fun addBook() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addBook(
                BookBean(System.currentTimeMillis(), "Book" + Random(100).nextInt())
            )
        }
    }

    fun deleteBook() {
        viewModelScope.launch(Dispatchers.IO) {
            booksLiveData1.value?.get(0)?.let { bookBean ->
                repository.delete(bookBean)
            }
        }
    }

}