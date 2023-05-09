package com.belyakov.recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.belyakov.recyclerview.databinding.ActivityMainBinding
import com.belyakov.recyclerview.model.User
import com.belyakov.recyclerview.model.UsersListener
import com.belyakov.recyclerview.model.UsersService

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UsersAdapter

    private val usersService: UsersService
        get() = (applicationContext as App).usersServices

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UsersAdapter(object : UserActionListener {

            override fun onUserMove(user: User, moveBy: Int) = usersService.moveUser(user, moveBy)

            override fun onUserDelete(user: User) = usersService.deleteUser(user)

            override fun onUserDetails(user: User) {

            }

            override fun onFireUser(user: User) = usersService.fireUser(user)

        })

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
        val itemAnimator = binding.recyclerView.itemAnimator
        if (itemAnimator is DefaultItemAnimator) {
            itemAnimator.supportsChangeAnimations = false
        }

        usersService.addListener(userListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        usersService.removeListener(userListener)
    }

    private val userListener: UsersListener = {
        adapter.users = it
    }
}