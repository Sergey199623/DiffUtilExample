package com.belyakov.recyclerview.presentation.screens.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.belyakov.recyclerview.data.model.User
import com.belyakov.recyclerview.data.model.UsersListener
import com.belyakov.recyclerview.data.model.UsersService
import kotlinx.coroutines.flow.MutableStateFlow

class UsersListViewModel(
    private val usersService: UsersService
) : ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private val listener: UsersListener = {
        _users.value = it
    }

    init {
        loadUsers()
    }

    override fun onCleared() {
        super.onCleared()
        usersService.removeListener(listener)
    }

    private fun loadUsers() = usersService.addListener(listener)

    fun moveUser(user: User, moveBy: Int) = usersService.moveUser(user, moveBy)

    fun deleteUser(user: User) = usersService.deleteUser(user)

    fun fireUser(user: User) = usersService.fireUser(user)
}