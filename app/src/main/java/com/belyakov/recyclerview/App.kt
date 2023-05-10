package com.belyakov.recyclerview

import android.app.Application
import com.belyakov.recyclerview.data.model.UsersService

class App : Application() {

    val usersServices = UsersService()
}