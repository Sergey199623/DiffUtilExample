package com.belyakov.recyclerview

import com.belyakov.recyclerview.data.model.User

interface Navigator {

    fun showDetails(user: User)
    fun goBack()
    fun showToast(message: Int)
}