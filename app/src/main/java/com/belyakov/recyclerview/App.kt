package com.belyakov.recyclerview

import android.app.Application
import com.belyakov.recyclerview.model.UsersService

class App : Application() {

    val usersServices = UsersService()
}