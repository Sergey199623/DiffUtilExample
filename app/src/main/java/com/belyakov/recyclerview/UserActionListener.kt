package com.belyakov.recyclerview

import com.belyakov.recyclerview.model.User

interface UserActionListener {

    fun onUserMove(user: User, moveBy: Int)
    fun onUserDelete(user: User)
    fun onUserDetails(user: User)
    fun onFireUser(user: User)
}