package com.belyakov.recyclerview.presentation.screens.viewModels

import androidx.lifecycle.ViewModel
import com.belyakov.recyclerview.tasks.Task

open class BaseViewModel : ViewModel() {

    private val tasks = mutableListOf<Task<*>>()

    override fun onCleared() {
        super.onCleared()
        tasks.forEach { it.cancel() }
    }

    fun <T> Task<T>.autoCancel() {
        tasks.add(this)
    }
}