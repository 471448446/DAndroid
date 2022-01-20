package com.test.hilt

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewMode @Inject constructor(
    val user: User
) : ViewModel(
) {
}