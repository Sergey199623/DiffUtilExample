package com.belyakov.recyclerview.presentation.screens.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.belyakov.recyclerview.UserNotFoundException
import com.belyakov.recyclerview.data.model.User
import com.belyakov.recyclerview.data.model.UserDetails
import com.belyakov.recyclerview.data.model.UsersService

class UserDetailsViewModel(
    private val usersService: UsersService
) : ViewModel() {

    private val _userDetails = MutableLiveData<UserDetails>()
    val userDetails: LiveData<UserDetails> = _userDetails

    fun loadUser(userId: Long) {
        if (userDetails.value != null) return
        try {
            _userDetails.value = usersService.getUserByIde(userId)
        } catch (e: UserNotFoundException) {
            e.printStackTrace()
        }
    }

    fun deleteUser() {
        val userDetails = this.userDetails.value ?: return
        usersService.deleteUser(userDetails.user)
    }
}