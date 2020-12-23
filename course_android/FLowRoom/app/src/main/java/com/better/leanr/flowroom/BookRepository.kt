package com.better.leanr.flowroom

class BookRepository {
    private val dao = Database.shared.bookDao()
    fun addBook(bookBean: BookBean) {
        dao.insert(bookBean)
    }

    fun books() = dao.select()

    fun delete(bookBean: BookBean) = dao.delete(bookBean)

}